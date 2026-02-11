/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Vehicle;

/**
 *
 * @author emman
 */
public class VehicleData {

    private final ArrayList<Vehicle> vehicles = new ArrayList<>();

    public void insertVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getPlate().equalsIgnoreCase(plate)) {
                return vehicle;
            }
        }
        return null;
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public void updateVehicle(Vehicle existing, Vehicle updated) {
        existing.setPlate(updated.getPlate());
        existing.setColor(updated.getColor());
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setOwners(updated.getOwners());
        existing.setType(updated.getType());
    }
}
