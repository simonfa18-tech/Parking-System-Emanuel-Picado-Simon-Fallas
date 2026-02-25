/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.VehicleTypeData;
import model.entities.VehicleType;

/**
 *
 * @author emman
 */
public class VehicleTypeController {

    private final VehicleTypeData vehicleTypeData = new VehicleTypeData();

    public String insertVehicleType(VehicleType vehicleType) {
        if (vehicleTypeData.findVehicleTypeById(vehicleType.getId()) == null) {
            vehicleTypeData.insertVehicleType(vehicleType);
            return "Vehicle type successfully entered";
        } else {
            return "The vehicle type was not inserted because it already exists";
        }
    }

    public ArrayList<VehicleType> getAllVehicleTypes() {
        return vehicleTypeData.getAllVehicleTypes();
    }

    public VehicleType findVehicleTypeById(int id) {
        return vehicleTypeData.findVehicleTypeById(id);
    }

    public String removeVehicleType(int id) {
        VehicleType vehicleTypeToRemove = vehicleTypeData.findVehicleTypeById(id);
        if (vehicleTypeToRemove != null) {
            vehicleTypeData.removeVehicleType(vehicleTypeToRemove);
            return "Type of vehicle successfully removed";
        } else {
            return "The vehicle type was not removed because it does not exist.";
        }
    }

    public String updateVehicleType(VehicleType vehicleType) {
        VehicleType existing = vehicleTypeData.findVehicleTypeById(vehicleType.getId());
        if (existing != null) {
            vehicleTypeData.updateVehicleType(existing, vehicleType);
            return "Vehicle type successfully updated";
        } else {
            return "The vehicle type could not be updated because it does not exist.";
        }
    }
}
