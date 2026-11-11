package com.shubham.carrental.entities;

import com.shubham.carrental.enums.VehicleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        this.vehicleByPlate = new HashMap<>();
        this.vehicleByType = new HashMap<>();
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
        return vehicleByType.get(vehicleType);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleByPlate.computeIfAbsent(vehicle.getVehiclePlate(), k -> vehicle);
        vehicleByType.computeIfAbsent(vehicle.getType(), k -> List.of(vehicle));
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
}