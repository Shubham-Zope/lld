package com.parkinglot.payment;

import com.parkinglot.enums.PaymentMode;
import java.util.EnumMap;
import java.util.Map;

public class PaymentProcessorFactory {
    private final Map<PaymentMode, PaymentProcessor> processors = new EnumMap<>(PaymentMode.class);

    public PaymentProcessorFactory() {
        for (PaymentMode mode : PaymentMode.values())
            processors.put(mode, new DemoPaymentProcessor());
    }

    public PaymentProcessor getProcessor(PaymentMode mode) {
        PaymentProcessor processor = processors.get(mode);
        if (processor == null)
            throw new IllegalArgumentException("Unsupported payment mode: " + mode);
        return processor;
    }
}
