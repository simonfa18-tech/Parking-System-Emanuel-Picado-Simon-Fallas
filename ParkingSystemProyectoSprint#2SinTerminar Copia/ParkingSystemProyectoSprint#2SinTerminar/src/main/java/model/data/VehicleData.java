/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.ParkingLot;
import model.entities.Vehicle;

public class VehicleData {

    public Vehicle findVehicleByPlate(String plate, ParkingLot lot) {
        if (lot == null) {
            return null;
        }
        for (Vehicle v : lot.getVehicles()) {
            if (v.getPlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }

    public void insertVehicle(Vehicle vehicle, ParkingLot lot) {
        if (lot == null) {
            return;
        }
        if (findVehicleByPlate(vehicle.getPlate(), lot) != null) {
            return;
        }
        lot.getVehicles().add(vehicle);
    }

    public ArrayList<Vehicle> getAllVehicles(ParkingLot lot) {
        return lot != null ? lot.getVehicles() : new ArrayList<>();
    }

    public void removeVehicle(Vehicle vehicle, ParkingLot lot) {
        if (lot == null) {
            return;
        }
        lot.getVehicles().remove(vehicle);
    }

    public void updateVehicle(Vehicle existing, Vehicle updated) {
        existing.setColor(updated.getColor());
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setOwners(updated.getOwners());
        existing.setVehicleType(updated.getVehicleType());
        existing.setSpace(updated.getSpace());
        existing.setEntryTime(updated.getEntryTime());
        existing.setExitTime(updated.getExitTime());
    }
}
