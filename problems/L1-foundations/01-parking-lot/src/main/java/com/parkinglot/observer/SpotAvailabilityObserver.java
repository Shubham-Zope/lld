package com.parkinglot.observer;
import com.parkinglot.model.ParkingSpot;
public interface SpotAvailabilityObserver {
    void onSpotChanged(ParkingSpot spot);
}
