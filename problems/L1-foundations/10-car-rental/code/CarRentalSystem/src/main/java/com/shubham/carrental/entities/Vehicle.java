package com.shubham.carrental.entities;

import com.shubham.carrental.enums.BookingStatus;
import com.shubham.carrental.enums.VehicleStatus;
import com.shubham.carrental.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
  private final String vehicleId;
  private final String vehiclePlate;
  private final VehicleType vehicleType;
  private VehicleStatus vehicleStatus;
  private BookingStatus  bookingStatus;
  private final List<BookingSlot> bookingSlots;

  public Vehicle(String vehicleId, String  vehiclePlate, VehicleType vehicleType) {
    this.vehicleId = vehicleId;
    this.vehiclePlate = vehiclePlate;
    this.vehicleType = vehicleType;
    this.vehicleStatus = VehicleStatus.AVAILABLE;
    this.bookingStatus = BookingStatus.NOT_BOOKED;
    this.bookingSlots = new ArrayList<>();
  }

  public String getId() {
    return vehicleId;
  }

  public String getVehiclePlate() {
    return vehiclePlate;
  }

  public VehicleType getType() {
    return vehicleType;
  }

  public synchronized VehicleStatus getVehicleStatus() {
      return vehicleStatus;
  }

  public int getVehicleDailyRate() {
    return vehicleType.dailyPrice;
  }

  public int getVehicleExtraDaysFine() {
      return vehicleType.extraDaysFine;
  }

  public int getDamagedVehicleFine() {
      return vehicleType.damagedFine;
  }

  public synchronized List<BookingSlot> getBookingSlots() {
    return List.copyOf(bookingSlots);
  }

  // Two slots overlap when each one starts before the other ends.
  public synchronized boolean isBookingAvailable(BookingSlot otherBookingSlot) {
      if (otherBookingSlot == null) {
          return false;
      }
      return bookingSlots.stream().noneMatch(existingSlot ->
              otherBookingSlot.startDate().before(existingSlot.endDate())
                      && existingSlot.startDate().before(otherBookingSlot.endDate())
      );
  }

  public synchronized boolean isBooked() {
    return bookingStatus == BookingStatus.BOOKED;
  }

  public synchronized boolean isReserved() {
      return bookingStatus == BookingStatus.RESERVED;
  }

  // Pick-up: vehicle leaves with the customer. Returns false if the slot clashes with another booking.
  public synchronized boolean bookVehicle(BookingSlot bookingSlot) {
      if (!bookingSlots.contains(bookingSlot)) {
          if (!isBookingAvailable(bookingSlot)) {
              return false;
          }
          bookingSlots.add(bookingSlot);
      }
      vehicleStatus = VehicleStatus.NOT_AVAILABLE;
      bookingStatus = BookingStatus.BOOKED;
      return true;
  }

  // Check and add happen under one lock, so two reservations can never take overlapping slots.
  public synchronized boolean reserveBooking(BookingSlot bookingSlot) {
    if (!isBookingAvailable(bookingSlot)) {
      return false;
    }
    bookingSlots.add(bookingSlot);
    bookingStatus = BookingStatus.RESERVED;
    return true;
  }

  // Moves a booking to a new slot; keeps the old slot if the new one is not free.
  public synchronized boolean rescheduleBooking(BookingSlot oldSlot, BookingSlot newSlot) {
    if (!bookingSlots.remove(oldSlot)) {
      return false;
    }
    if (!isBookingAvailable(newSlot)) {
      bookingSlots.add(oldSlot);
      return false;
    }
    bookingSlots.add(newSlot);
    return true;
  }

  public synchronized void cancelBooking(BookingSlot bookingSlot) {
    bookingSlots.remove(bookingSlot);
    bookingStatus = bookingSlots.isEmpty() ? BookingStatus.NOT_BOOKED : BookingStatus.RESERVED;
  }

  public synchronized void markVehicleAsAvailable() {
      vehicleStatus = VehicleStatus.AVAILABLE;
  }

  public synchronized void markVehicleAsDamaged() {
      vehicleStatus = VehicleStatus.DAMAGED;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
