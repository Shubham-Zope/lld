package com.parkinglot.observer;

import com.parkinglot.enums.SpotType;
import com.parkinglot.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayBoard implements SpotAvailabilityObserver {
    private final Map<String, Map<SpotType, Integer>> available = new ConcurrentHashMap<>();

    @Override
    public void onSpotChanged(ParkingSpot spot) {
        // The floor is registered by the caller using updateFloorSnapshot.
    }

    public void updateFloorSnapshot(Floor floor) {
        Map<SpotType, Integer> counts = new EnumMap<>(SpotType.class);
        for (ParkingSpot spot : floor.getSpots()) {
            if (spot.isAvailable())
                counts.merge(spot.getType(), 1, Integer::sum);
        }
        available.put(floor.getId(), Map.copyOf(counts));
    }

    public Map<SpotType, Integer> getAvailability(String floorId) {
        return available.getOrDefault(floorId, Map.of());
    }
}
