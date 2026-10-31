package com.parkinglot;

import com.parkinglot.enums.*;
import com.parkinglot.model.*;
import com.parkinglot.observer.DisplayBoard;
import com.parkinglot.payment.PaymentProcessorFactory;
import com.parkinglot.service.ParkingLotService;
import com.parkinglot.strategy.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ParkingSpot compact = new ParkingSpot("C-1", SpotType.COMPACT);
        ParkingSpot regular = new ParkingSpot("R-1", SpotType.REGULAR);
        ParkingSpot large = new ParkingSpot("L-1", SpotType.LARGE);
        ParkingSpot ev = new ParkingSpot("EV-1", SpotType.EV);

        Floor floor = new Floor("F-1", 1, List.of(compact, regular, large, ev));
        Gate entry = new Gate("ENTRY-1", GateType.ENTRY);
        Gate exit = new Gate("EXIT-1", GateType.EXIT);
        ParkingLot lot = new ParkingLot("LOT-1", "Central Parking", List.of(floor), List.of(entry, exit));

        DisplayBoard board = new DisplayBoard();
        ParkingLotService service = new ParkingLotService(
                lot, new DefaultCompatibilityPolicy(), new NearestSpotAllocationStrategy(),
                new HourlyPricingStrategy(new BigDecimal("30")),
                new PaymentProcessorFactory(), board, Clock.systemDefaultZone());

        Vehicle car = new Vehicle("V-1", "MH01AB1234", VehicleType.CAR, Set.of());
        Ticket ticket = service.park(car, entry.getId());
        System.out.println("Ticket created: " + ticket.getId() + ", spot: " + ticket.getSpot().getId());

        Receipt receipt = service.unpark(ticket.getId(), exit.getId(), PaymentMode.UPI);
        System.out.println("Receipt: " + receipt.getId() + ", fee: " + receipt.getFee());
        System.out.println("Available spots: " + service.getFloorAvailability(floor.getId()));
    }
}
