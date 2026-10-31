package com.parkinglot.model;
import com.parkinglot.enums.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;
public class Ticket {
    private final String id = UUID.randomUUID().toString();
    private final ParkingSpot spot;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private final Gate entryGate;
    private TicketStatus status = TicketStatus.ACTIVE;
    public Ticket(ParkingSpot spot, Vehicle vehicle, LocalDateTime entryTime, Gate entryGate) {
        this.spot = spot; this.vehicle = vehicle; this.entryTime = entryTime; this.entryGate = entryGate;
    }
    public synchronized boolean transition(TicketStatus expected, TicketStatus next) {
        if (status != expected) return false; status = next; return true;
    }
    public synchronized TicketStatus getStatus() { return status; }
    public String getId() { return id; }
    public ParkingSpot getSpot() { return spot; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Gate getEntryGate() { return entryGate; }
}
