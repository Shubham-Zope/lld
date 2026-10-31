package com.parkinglot.strategy;

import com.parkinglot.model.Ticket;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PricingStrategy {
    BigDecimal calculateFee(Ticket ticket, LocalDateTime exitTime);
}
