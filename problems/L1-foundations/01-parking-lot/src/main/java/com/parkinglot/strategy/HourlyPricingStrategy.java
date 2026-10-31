package com.parkinglot.strategy;

import com.parkinglot.model.Ticket;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {
    private final BigDecimal hourlyRate;

    public HourlyPricingStrategy(BigDecimal hourlyRate) {
        if (hourlyRate == null || hourlyRate.signum() < 0)
            throw new IllegalArgumentException("Invalid hourly rate");
        this.hourlyRate = hourlyRate;
    }

    @Override
    public BigDecimal calculateFee(Ticket ticket, LocalDateTime exitTime) {
        long minutes = Math.max(0, Duration.between(ticket.getEntryTime(), exitTime).toMinutes());
        long billableHours = Math.max(1, (minutes + 59) / 60);
        return hourlyRate.multiply(BigDecimal.valueOf(billableHours));
    }
}
