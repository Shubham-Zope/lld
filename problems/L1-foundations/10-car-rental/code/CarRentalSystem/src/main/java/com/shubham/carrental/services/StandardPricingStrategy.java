package com.shubham.carrental.services;

import com.shubham.carrental.entities.Reservation;
import com.shubham.carrental.enums.VehicleStatus;

public class StandardPricingStrategy implements PricingStrategy {
    @Override()
    public double calculatePrice(Reservation reservation) {
        boolean damaged = reservation.getVehicle().getVehicleStatus() == VehicleStatus.DAMAGED;

        return reservation.getVehicle().getVehicleDailyRate()
                * reservation.getBookingDays() + reservation.getVehicle().getVehicleExtraDaysFine() * reservation.getExtraDaysAdded()
                + (damaged ? reservation.getVehicle().getDamagedVehicleFine() : 0);
    }
}
