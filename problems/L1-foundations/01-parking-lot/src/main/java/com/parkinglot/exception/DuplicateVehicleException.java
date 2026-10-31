package com.parkinglot.exception;

public class DuplicateVehicleException extends RuntimeException {
    public DuplicateVehicleException(String registration) {
        super("Vehicle is already parked: " + registration);
    }
}
