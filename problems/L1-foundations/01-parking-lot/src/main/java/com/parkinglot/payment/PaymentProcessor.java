package com.parkinglot.payment;

import com.parkinglot.model.Payment;
import java.math.BigDecimal;

public interface PaymentProcessor {
    Payment process(Payment payment, BigDecimal amount);
}
