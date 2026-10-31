package com.parkinglot.service;

import com.parkinglot.enums.*;
import com.parkinglot.exception.*;
import com.parkinglot.model.*;
import com.parkinglot.observer.DisplayBoard;
import com.parkinglot.payment.*;
import com.parkinglot.strategy.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotService {
    private final ParkingLot parkingLot;
    private final CompatibilityPolicy compatibilityPolicy;
    private final SpotAllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;
    private final PaymentProcessorFactory paymentFactory;
    private final DisplayBoard displayBoard;
    private final Clock clock;

    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();
    private final Map<String, String> activeTicketByRegistration = new ConcurrentHashMap<>();

    public ParkingLotService(
        ParkingLot parkingLot, 
        CompatibilityPolicy compatibilityPolicy,
        SpotAllocationStrategy allocationStrategy, 
        PricingStrategy pricingStrategy,
        PaymentProcessorFactory paymentFactory, 
        DisplayBoard displayBoard, 
        Clock clock
    ) {
        this.parkingLot = parkingLot;
        this.compatibilityPolicy = compatibilityPolicy;
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.paymentFactory = paymentFactory;
        this.displayBoard = displayBoard;
        this.clock = clock;
        parkingLot.getFloors().forEach(displayBoard::updateFloorSnapshot);
    }

    public Ticket park(Vehicle vehicle, String entryGateId) {
        Gate gate = parkingLot.getGateById(entryGateId)
                .filter(g -> g.getType() == GateType.ENTRY)
                .orElseThrow(() -> new IllegalArgumentException("Invalid entry gate: " + entryGateId));

        String registration = vehicle.getRegistrationNumber();
        String reservation = UUID.randomUUID().toString();
        if (activeTicketByRegistration.putIfAbsent(registration, reservation) != null)
            throw new DuplicateVehicleException(registration);

        ParkingSpot spot = null;
        try {
            spot = allocationStrategy.allocate(vehicle, parkingLot.getFloors(), compatibilityPolicy);
            Ticket ticket = new Ticket(spot, vehicle, LocalDateTime.now(clock), gate);
            tickets.put(ticket.getId(), ticket);
            activeTicketByRegistration.put(registration, ticket.getId());
            refreshBoards();
            return ticket;
        } catch (RuntimeException e) {
            if (spot != null && spot.getCurrentVehicle() == vehicle)
                spot.release(registration);
            activeTicketByRegistration.remove(registration, reservation);
            throw e;
        }
    }

    public Receipt unpark(String ticketId, String exitGateId, PaymentMode mode) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket == null)
            throw new InvalidTicketException(ticketId);

        Gate exitGate = parkingLot.getGateById(exitGateId)
                .filter(g -> g.getType() == GateType.EXIT)
                .orElseThrow(() -> new IllegalArgumentException("Invalid exit gate: " + exitGateId));

        if (!ticket.transition(TicketStatus.ACTIVE, TicketStatus.PAYMENT_PENDING))
            throw new InvalidTicketException(ticketId);

        LocalDateTime exitTime = LocalDateTime.now(clock);
        BigDecimal fee = pricingStrategy.calculateFee(ticket, exitTime);
        Payment payment = new Payment(mode);
        PaymentProcessor processor = paymentFactory.getProcessor(mode);

        try {
            processor.process(payment, fee);
            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                ticket.transition(TicketStatus.PAYMENT_PENDING, TicketStatus.ACTIVE);
                throw new PaymentFailedException("Payment did not complete successfully");
            }

            Receipt receipt = new Receipt(ticket, exitGate, exitTime, fee, payment);
            ticket.transition(TicketStatus.PAYMENT_PENDING, TicketStatus.PAID);
            ticket.getSpot().release(ticket.getVehicle().getRegistrationNumber());
            ticket.transition(TicketStatus.PAID, TicketStatus.COMPLETED);
            activeTicketByRegistration.remove(ticket.getVehicle().getRegistrationNumber(), ticketId);
            refreshBoards();
            return receipt;
        } catch (RuntimeException e) {
            if (ticket.getStatus() == TicketStatus.PAYMENT_PENDING)
                ticket.transition(TicketStatus.PAYMENT_PENDING, TicketStatus.ACTIVE);
            throw e;
        }
    }

    public Optional<Ticket> getTicket(String ticketId) {
        return Optional.ofNullable(tickets.get(ticketId));
    }

    public void markSpotOutOfService(String spotId) {
        ParkingSpot spot = parkingLot.getAllSpots().stream()
                .filter(s -> s.getId().equals(spotId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown spot: " + spotId));
        spot.markOutOfService();
        refreshBoards();
    }

    public void restoreSpot(String spotId) {
        ParkingSpot spot = parkingLot.getAllSpots().stream()
                .filter(s -> s.getId().equals(spotId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown spot: " + spotId));
        spot.markActive();
        refreshBoards();
    }

    public Map<com.parkinglot.enums.SpotType, Integer> getFloorAvailability(String floorId) {
        return displayBoard.getAvailability(floorId);
    }

    private void refreshBoards() {
        parkingLot.getFloors().forEach(displayBoard::updateFloorSnapshot);
    }
}
