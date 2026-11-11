package com.shubham.carrental.entities;

public class Customer {
    private final String customerId;
    private final String customerName;
    private Vehicle reservedVehicle;
    private BookingSlot reservedBookingSlot;
    private Vehicle bookedVehicle;
    private BookingSlot bookedBookingSlot;

    public Customer(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;

        this.reservedVehicle = null;
        this.reservedBookingSlot = null;
        this.bookedVehicle = null;
        this.bookedBookingSlot = null;
    }

    public BookingSlot getBookedBookingSlot() {
        return bookedBookingSlot;
    }

    public BookingSlot getReservedBookingSlot() {
        return reservedBookingSlot;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Vehicle getReservedVehicle() {
        return reservedVehicle;
    }

    public Vehicle getBookedVehicle() {
        return bookedVehicle;
    }

    // Pass nulls to clear.
    public void setReservation(Vehicle vehicle, BookingSlot bookingSlot) {
        this.reservedVehicle = vehicle;
        this.reservedBookingSlot = bookingSlot;
    }

    // Pass nulls to clear.
    public void setBooking(Vehicle vehicle, BookingSlot bookingSlot) {
        this.bookedVehicle = vehicle;
        this.bookedBookingSlot = bookingSlot;
    }
}
