package com.parkinglot.model;
import com.parkinglot.enums.VehicleType;
import java.util.Set;
public class Vehicle {
    private final String id, registrationNumber;
    private final VehicleType type;
    private final Set<String> features;
    public Vehicle(String id, String registrationNumber, VehicleType type, Set<String> features) {
        if (id == null || id.isBlank() || registrationNumber == null || registrationNumber.isBlank() || type == null)
            throw new IllegalArgumentException("Vehicle id, registration and type are required");
        this.id = id; this.registrationNumber = registrationNumber; this.type = type;
        this.features = features == null ? Set.of() : Set.copyOf(features);
    }
    public String getId() { return id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public VehicleType getType() { return type; }
    public Set<String> getFeatures() { return features; }
}
