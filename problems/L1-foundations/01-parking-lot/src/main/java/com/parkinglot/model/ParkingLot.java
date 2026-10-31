package com.parkinglot.model;

import com.parkinglot.enums.GateType;
import java.util.*;

public class ParkingLot {
    private final String id, name;
    private final List<Floor> floors;
    private final List<Gate> gates;

    public ParkingLot(String id, String name, List<Floor> floors, List<Gate> gates) {
        this.id = id;
        this.name = name;
        this.floors = new ArrayList<>(floors);
        this.gates = new ArrayList<>(gates);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Floor> getFloors() {
        return List.copyOf(floors);
    }

    public List<Gate> getGates() {
        return List.copyOf(gates);
    }

    public Optional<Floor> getFloorByNumber(int n) {
        return floors.stream().filter(f -> f.getFloorNumber() == n).findFirst();
    }

    public Optional<Gate> getGateById(String id) {
        return gates.stream().filter(g -> g.getId().equals(id)).findFirst();
    }

    public List<Gate> getGatesByType(GateType type) {
        return gates.stream().filter(g -> g.getType() == type).toList();
    }

    public List<ParkingSpot> getAllSpots() {
        return floors.stream().flatMap(f -> f.getSpots().stream()).toList();
    }
}
