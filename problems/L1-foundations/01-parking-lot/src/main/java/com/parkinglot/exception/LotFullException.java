package com.parkinglot.exception;

import com.parkinglot.enums.VehicleType;

public class LotFullException extends RuntimeException {
    public LotFullException(VehicleType type) {
        super("No compatible parking spot available for " + type);
    }
}
