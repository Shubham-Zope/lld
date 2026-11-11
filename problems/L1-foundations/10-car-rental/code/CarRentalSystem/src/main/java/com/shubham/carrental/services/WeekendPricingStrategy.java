package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;
import com.shubham.carrental.enums.VehicleStatus;

public class WeekendPricingStrategy implements PricingStrategy {
    @Override()
    public double calculatePrice(Reservation reservation) {
        // Weekend surcharge applies to the daily rate only, not to fines.
        double rentalPrice = reservation.getVehicle().getVehicleDailyRate() * reservation.getBookingDays() * 1.5;
        boolean damaged = reservation.getVehicle().getVehicleStatus() == VehicleStatus.DAMAGED;

        return rentalPrice
                + reservation.getVehicle().getVehicleExtraDaysFine() * reservation.getExtraDaysAdded()
                + (damaged ? reservation.getVehicle().getDamagedVehicleFine() : 0);
    }
}
