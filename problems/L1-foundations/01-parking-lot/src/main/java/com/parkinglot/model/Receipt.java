package com.parkinglot.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
public class Receipt {
    private final String id = UUID.randomUUID().toString();
    private final Ticket ticket;
    private final Gate exitGate;
    private final LocalDateTime exitTime;
    private final BigDecimal fee;
    private final Payment payment;
    public Receipt(Ticket ticket, Gate exitGate, LocalDateTime exitTime, BigDecimal fee, Payment payment) {
        this.ticket = ticket; this.exitGate = exitGate; this.exitTime = exitTime; this.fee = fee; this.payment = payment;
    }
    public String getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public Gate getExitGate() { return exitGate; }
    public LocalDateTime getExitTime() { return exitTime; }
    public BigDecimal getFee() { return fee; }
    public Payment getPayment() { return payment; }
}
