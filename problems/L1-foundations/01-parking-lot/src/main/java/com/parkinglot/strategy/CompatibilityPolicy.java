package com.parkinglot.strategy;

import com.parkinglot.model.*;

public interface CompatibilityPolicy {
    boolean canFit(Vehicle vehicle, ParkingSpot spot);
}
