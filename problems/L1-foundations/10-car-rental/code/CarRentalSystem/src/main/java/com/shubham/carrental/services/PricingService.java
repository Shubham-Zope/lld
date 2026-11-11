package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;
import com.shubham.carrental.enums.AddOns;

import java.util.List;

public class PricingService {
    private final PricingStrategy pricingStrategy;

    public PricingService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculatePrice(Reservation reservation) {
        double basePrice = pricingStrategy.calculatePrice(reservation);

        PriceCalculator calculator = new BasePrice(basePrice);
        List<AddOns> addOns = reservation.getAddOns();

        for (AddOns addOn : addOns) {
            calculator = switch (addOn) {
                case AddOns.GPS -> (new GPSPriceCalculator(calculator));
                case AddOns.INSURANCE -> (new InsurancePriceCalculator(calculator));
            };
        }

        return calculator.calculate();
    }
}
