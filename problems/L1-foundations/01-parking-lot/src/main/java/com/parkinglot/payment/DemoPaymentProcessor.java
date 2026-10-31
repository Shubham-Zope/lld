package com.parkinglot.payment;

import com.parkinglot.model.Payment;
import com.parkinglot.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;

public class DemoPaymentProcessor implements PaymentProcessor {
    @Override
    public Payment process(Payment payment, BigDecimal amount) {
        payment.updateStatus(PaymentStatus.IN_PROGRESS);
        // Replace this simulated success with the real payment gateway integration.
        payment.updateReference("TXN-" + UUID.randomUUID());
        payment.updateStatus(PaymentStatus.SUCCESS);
        return payment;
    }
}
