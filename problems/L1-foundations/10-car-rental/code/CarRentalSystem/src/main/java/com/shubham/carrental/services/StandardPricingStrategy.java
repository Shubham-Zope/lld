package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;

public class StandardPricingStrategy implements PricingStrategy {
    @Override()
    public double calculatePrice(Reservation reservation) {
        return reservation.getVehicle().getVehicleDailyRate()
                * reservation.getBookingDays() + reservation.getVehicle().getVehicleExtraDaysFine() * reservation.getExtraDaysAdded()
                + reservation.getVehicle().getDamagedVehicleFine();
    }
}
