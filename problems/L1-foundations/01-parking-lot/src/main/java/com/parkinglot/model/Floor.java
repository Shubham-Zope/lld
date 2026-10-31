package com.parkinglot.model;

import java.util.*;

public class Floor {
    private final String id;
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public Floor(String id, int floorNumber, List<ParkingSpot> spots) {
        if (id == null || id.isBlank() || floorNumber < 0)
            throw new IllegalArgumentException("Invalid floor");
        this.id = id;
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>(spots);
    }

    public String getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return List.copyOf(spots);
    }

    public Optional<ParkingSpot> getSpotById(String spotId) {
        return spots.stream().filter(s -> s.getId().equals(spotId)).findFirst();
    }
}
