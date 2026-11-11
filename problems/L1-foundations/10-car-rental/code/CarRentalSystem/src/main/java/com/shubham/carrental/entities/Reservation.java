package com.shubham.carrental.entities;

import com.shubham.carrental.enums.AddOns;
import com.shubham.carrental.enums.BookingStatus;
import com.shubham.carrental.services.PricingService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Branch branch;
    private final Vehicle vehicle;
    private BookingSlot bookingSlot;
    private BookingStatus status;
    private final List<AddOns> addOns;
    private int extraDaysAdded;

    private final PricingService pricingService;

    public Reservation(String reservationId, Customer customer, Branch branch, Vehicle vehicle, BookingSlot bookingSlot, PricingService pricingService) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.branch = branch;
        this.vehicle = vehicle;
        this.bookingSlot = bookingSlot;
        this.status = BookingStatus.NOT_BOOKED;
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

    public BookingStatus getStatus() {
        return status;
    }

    public boolean bookVehicle() {
        boolean booked = this.vehicle.bookVehicle(this.bookingSlot);
        if (booked) {
            this.status = BookingStatus.BOOKED;
        }
        return booked;
    }

    public boolean reserveVehicle() {
        boolean reserved = this.vehicle.reserveBooking(this.bookingSlot);
        if (reserved) {
            this.status = BookingStatus.RESERVED;
        }
        return reserved;
    }

    // Returns false (and keeps the current slot) if the vehicle is not free for the new dates.
    public boolean modifyBookingSlot(BookingSlot newBookingSlot) {
        if (!this.vehicle.rescheduleBooking(this.bookingSlot, newBookingSlot)) {
            return false;
        }
        this.bookingSlot = newBookingSlot;
        return true;
    }

    public void cancelReservation() {
        this.vehicle.cancelBooking(this.bookingSlot);
        this.status = BookingStatus.NOT_BOOKED;
    }

    public double calculatePrice() {
        return this.pricingService.calculatePrice(this);
    }

    public int getBookingDays() {
        // Count calendar days in the local zone (a 23h/25h DST day is still one day); any partial day counts as a full day.
        ZonedDateTime start = bookingSlot.startDate().toInstant().atZone(ZoneId.systemDefault());
        ZonedDateTime end = bookingSlot.endDate().toInstant().atZone(ZoneId.systemDefault());

        long days = ChronoUnit.DAYS.between(start, end);
        if (start.plusDays(days).isBefore(end)) {
            days++;
        }

        return (int) days + extraDaysAdded;
    }

    public int getExtraDaysAdded() {
        return extraDaysAdded;
    }

    public void setExtraDaysAdded(int extraDaysAdded) {
        this.extraDaysAdded = extraDaysAdded;
    }
}
