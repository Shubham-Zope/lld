package com.shubham.carrental.services;

import com.shubham.carrental.enums.AddOns;

public class GPSPriceCalculator implements PriceCalculator {
    private final PriceCalculator calculator;

    public GPSPriceCalculator(PriceCalculator calculator) {
        this.calculator = calculator;
    }

    @Override()
    public double calculate() {
        return calculator.calculate() + AddOns.GPS.addOnPrice;
    }
}
