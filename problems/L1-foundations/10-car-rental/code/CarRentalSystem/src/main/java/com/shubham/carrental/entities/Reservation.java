package com.shubham.carrental.entities;

import com.shubham.carrental.enums.AddOns;
import com.shubham.carrental.services.PricingService;
import com.shubham.carrental.services.PricingStrategy;

import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Branch branch;
    private final Vehicle vehicle;
    private final BookingSlot bookingSlot;
    private final List<AddOns> addOns;
    private int extraDaysAdded;

    private final PricingService pricingService;

    public Reservation(String reservationId, Customer customer, Branch branch, Vehicle vehicle, BookingSlot bookingSlot, PricingService pricingService) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.branch = branch;
        this.vehicle = vehicle;
        this.bookingSlot = bookingSlot;
        this.addOns = new ArrayList<>();
        this.extraDaysAdded = 0;
        this.pricingService = pricingService;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Branch getBranch() {
        return branch;
    }

    public List<AddOns> getAddOns() {
        return addOns;
    }

    public void addAddOns(AddOns addOns) {
        this.addOns.add(addOns);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public BookingSlot getBookingSlot() {
        return bookingSlot;
    }

    public void vehicleIsDamaged() {
        this.vehicle.markVehicleAsDamaged();
    }

    public void bookVehicle() {
        this.vehicle.bookVehicle(this.bookingSlot);
    }

    public void reserveVehicle() {
        this.vehicle.reserveBooking(this.bookingSlot);
    }

    public void cancelReservation() {
        this.vehicle.cancelBooking(this.bookingSlot);
    }

    public double calculatePrice() {
        return this.pricingService.calculatePrice(this);
    }

    public int getBookingDays() {
        long diffInMillis = bookingSlot.endDate().getTime()
                - bookingSlot.startDate().getTime();

        int days = (int) Math.ceil(
                diffInMillis / (1000.0 * 60 * 60 * 24)
        );

        return days + extraDaysAdded;
    }

    public int getExtraDaysAdded() {
        return extraDaysAdded;
    }

    public void setExtraDaysAdded(int extraDaysAdded) {
        this.extraDaysAdded = extraDaysAdded;
    }
}
