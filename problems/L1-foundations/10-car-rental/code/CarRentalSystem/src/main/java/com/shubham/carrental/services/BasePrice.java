package com.shubham.carrental.services;

public class BasePrice implements PriceCalculator {
    private final double  basePrice;

    public BasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public double calculate() {
        return basePrice;
    }
}
