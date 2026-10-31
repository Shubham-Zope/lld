package com.parkinglot.model;

import com.parkinglot.enums.*;
import java.util.UUID;

public class Payment {
    private final String id = UUID.randomUUID().toString();
    private final PaymentMode mode;
    private volatile PaymentStatus status = PaymentStatus.INITIATED;
    private volatile String transactionReference;

    public Payment(PaymentMode mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public PaymentMode getMode() {
        return mode;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void updateReference(String reference) {
        this.transactionReference = reference;
    }
}
