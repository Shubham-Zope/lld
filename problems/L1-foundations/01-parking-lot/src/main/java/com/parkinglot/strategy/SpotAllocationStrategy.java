package com.parkinglot.strategy;

import com.parkinglot.model.*;
import java.util.List;

public interface SpotAllocationStrategy {
    ParkingSpot allocate(Vehicle vehicle, List<Floor> floors, CompatibilityPolicy policy);
}
