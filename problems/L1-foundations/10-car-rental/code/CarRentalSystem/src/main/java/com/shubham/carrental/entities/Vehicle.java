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

  public VehicleStatus getVehicleStatus() {
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

  public List<BookingSlot> getBookingSlots() {
    return List.copyOf(bookingSlots);
  }

  public boolean isBookingAvailable(BookingSlot otherBookingSlot) {
      if (otherBookingSlot == null) {
          return false;
      }
      return bookingSlots.stream().noneMatch(existingSlot ->
              !otherBookingSlot.startDate().after(existingSlot.startDate())
                      && !otherBookingSlot.endDate().before(existingSlot.endDate())
      );
  }

  public boolean isBooked() {
    return bookingStatus == BookingStatus.BOOKED;
  }

  public boolean isReserved() {
      return bookingStatus == BookingStatus.RESERVED;
  }

  public void bookVehicle(BookingSlot bookingSlot) {
      bookingSlots.add(bookingSlot);
      vehicleStatus = VehicleStatus.NOT_AVAILABLE;
      bookingStatus = BookingStatus.BOOKED;
  }

  public void reserveBooking(BookingSlot bookingSlot) {
    bookingSlots.add(bookingSlot);
    vehicleStatus = VehicleStatus.NOT_AVAILABLE;
    bookingStatus = BookingStatus.RESERVED;
  }

  public void cancelBooking(BookingSlot bookingSlot) {
    bookingSlots.remove(bookingSlot);
    vehicleStatus = VehicleStatus.AVAILABLE;
    bookingStatus = BookingStatus.NOT_BOOKED;
  }

  public void markVehicleAsAvailable() {
      vehicleStatus = VehicleStatus.AVAILABLE;
  }

  public void markVehicleAsDamaged() {
      vehicleStatus = VehicleStatus.DAMAGED;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
