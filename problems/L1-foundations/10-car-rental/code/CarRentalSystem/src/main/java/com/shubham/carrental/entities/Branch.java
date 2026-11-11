package com.shubham.carrental.entities;

import com.shubham.carrental.enums.VehicleStatus;
import com.shubham.carrental.enums.VehicleType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Branch {
    private final String branchId;
    private final String branchName;
    private final List<Vehicle> vehicles;
    private final List<Customer> customers;

    private final Map<String, Vehicle> vehicleByPlate;
    private final Map<VehicleType, List<Vehicle>>  vehicleByType;

    public Branch(String branchId, String branchName, List<Vehicle> vehicles, List<Customer> customers) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.vehicles = new CopyOnWriteArrayList<>(vehicles);
        this.customers = new CopyOnWriteArrayList<>(customers);

        this.vehicleByPlate = new ConcurrentHashMap<>();
        this.vehicleByType = new ConcurrentHashMap<>();
        this.vehicles.forEach(this::indexVehicle);
    }

    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Vehicle searchVehicleByPlate(String vehiclePlate) {
        return vehicleByPlate.get(vehiclePlate);
    }

    public List<Vehicle> searchVehicleByType(VehicleType vehicleType) {
        return vehicleByType.getOrDefault(vehicleType, List.of());
    }

    // Vehicles of this type at this branch that are free for the whole date range and not damaged.
    public List<Vehicle> searchAvailableVehicles(VehicleType vehicleType, BookingSlot bookingSlot) {
        return searchVehicleByType(vehicleType).stream()
                .filter(vehicle -> vehicle.getVehicleStatus() != VehicleStatus.DAMAGED)
                .filter(vehicle -> vehicle.isBookingAvailable(bookingSlot))
                .toList();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        indexVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    private void indexVehicle(Vehicle vehicle) {
        vehicleByPlate.put(vehicle.getVehiclePlate(), vehicle);
        vehicleByType.computeIfAbsent(vehicle.getType(), k -> new CopyOnWriteArrayList<>()).add(vehicle);
    }
}
