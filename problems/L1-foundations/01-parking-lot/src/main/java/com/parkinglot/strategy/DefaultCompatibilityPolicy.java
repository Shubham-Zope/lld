package com.parkinglot.strategy;

import com.parkinglot.enums.*;
import com.parkinglot.model.*;
import java.util.*;

public class DefaultCompatibilityPolicy implements CompatibilityPolicy {
    private final Map<VehicleType, Set<SpotType>> compatibility = Map.of(
            VehicleType.MOTORCYCLE, Set.of(SpotType.COMPACT, SpotType.REGULAR, SpotType.LARGE),
            VehicleType.CAR, Set.of(SpotType.REGULAR, SpotType.LARGE),
            VehicleType.VAN, Set.of(SpotType.LARGE),
            VehicleType.TRUCK, Set.of(SpotType.LARGE));

    @Override
    public boolean canFit(Vehicle vehicle, ParkingSpot spot) {
        if (spot.getType() == SpotType.EV)
            return vehicle.getFeatures().contains("EV");
        return compatibility.getOrDefault(vehicle.getType(), Set.of()).contains(spot.getType());
    }
}
