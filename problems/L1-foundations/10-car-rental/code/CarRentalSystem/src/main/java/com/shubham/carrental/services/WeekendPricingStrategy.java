package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;

public class WeekendPricingStrategy implements PricingStrategy {
    @Override()
    public double calculatePrice(Reservation reservation) {
        double price = reservation.getVehicle().getVehicleDailyRate()
                * reservation.getBookingDays() + reservation.getVehicle().getVehicleExtraDaysFine() * reservation.getExtraDaysAdded()
                + reservation.getVehicle().getDamagedVehicleFine();

        return price * 1.5;
    }
}
