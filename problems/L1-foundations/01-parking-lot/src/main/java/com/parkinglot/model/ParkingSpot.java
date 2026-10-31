package com.parkinglot.model;

import com.parkinglot.enums.*;

public class ParkingSpot {
    private final String id;
    private final SpotType type;
    private SpotStatus status = SpotStatus.AVAILABLE;
    private SpotOperationalStatus operationalStatus = SpotOperationalStatus.ACTIVE;
    private Vehicle currentVehicle;

    public ParkingSpot(String id, SpotType type) {
        if (id == null || id.isBlank() || type == null)
            throw new IllegalArgumentException("Invalid spot");
        this.id = id;
        this.type = type;
    }

    public synchronized boolean tryOccupy(Vehicle vehicle) {
        if (vehicle == null || !isAvailable())
            return false;
        currentVehicle = vehicle;
        status = SpotStatus.OCCUPIED;
        return true;
    }

    public synchronized void release(String registrationNumber) {
        if (status != SpotStatus.OCCUPIED || currentVehicle == null ||
                !currentVehicle.getRegistrationNumber().equals(registrationNumber))
            throw new IllegalStateException("Vehicle does not own this occupied spot");
        currentVehicle = null;
        status = SpotStatus.AVAILABLE;
    }

    public synchronized void markOutOfService() {
        operationalStatus = SpotOperationalStatus.OUT_OF_SERVICE;
    }

    public synchronized void markActive() {
        operationalStatus = SpotOperationalStatus.ACTIVE;
    }

    public synchronized boolean isAvailable() {
        return status == SpotStatus.AVAILABLE && operationalStatus == SpotOperationalStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public synchronized SpotStatus getStatus() {
        return status;
    }

    public synchronized SpotOperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    public synchronized Vehicle getCurrentVehicle() {
        return currentVehicle;
    }
}
