package com.shubham.carrental.enums;

public enum VehicleType {
    CAR(300, 150, 2500),
    TRUCK(1000, 500, 5000),
    BIKE(150, 75, 1500),
    SCOOTER(100, 50, 1000),
    BUS(2500, 1250, 8000);

    public final int dailyPrice;
    public final int extraDaysFine;
    public final int damagedFine;

    VehicleType(int dailyPrice, int extraDaysFine, int damagedFine) {
        this.dailyPrice = dailyPrice;
        this.extraDaysFine = extraDaysFine;
        this.damagedFine = damagedFine;
    }
}