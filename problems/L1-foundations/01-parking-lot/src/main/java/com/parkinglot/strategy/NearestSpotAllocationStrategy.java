package com.parkinglot.strategy;

import com.parkinglot.model.*;
import com.parkinglot.exception.LotFullException;
import java.util.Comparator;
import java.util.List;

public class NearestSpotAllocationStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot allocate(Vehicle vehicle, List<Floor> floors, CompatibilityPolicy policy) {
        List<ParkingSpot> candidates = floors.stream().flatMap(f -> f.getSpots().stream())
                .filter(ParkingSpot::isAvailable)
                .filter(s -> policy.canFit(vehicle, s))
                .sorted(Comparator.comparingInt((ParkingSpot s) -> floors.stream()
                        .filter(f -> f.getSpots().contains(s)).findFirst().map(Floor::getFloorNumber)
                        .orElse(Integer.MAX_VALUE))
                        .thenComparing(ParkingSpot::getId))
                .toList();
        for (ParkingSpot spot : candidates) {
            if (spot.tryOccupy(vehicle))
                return spot;
        }
        throw new LotFullException(vehicle.getType());
    }
}
