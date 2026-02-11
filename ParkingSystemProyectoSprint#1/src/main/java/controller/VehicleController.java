/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.VehicleData;
import model.entities.Vehicle;

/**
 *
 * @author emman
 */
public class VehicleController {

    private final VehicleData vehicleData = new VehicleData();

  public String insertVehicle(Vehicle vehicle) {
    if (vehicleData.findVehicleByPlate(vehicle.getPlate()) == null) {
        vehicle.setEntryTime(LocalDateTime.now());
        vehicleData.insertVehicle(vehicle);
        return "Vehículo insertado con éxito";
    } else {
        return "No se insertó el vehículo porque ya existe en la base de datos";
    }
}

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicleData.getAllVehicles();
    }

    public Vehicle findVehicleByPlate(String plate) {
        return vehicleData.findVehicleByPlate(plate);
    }

    public String removeVehicle(String plate) {
        Vehicle vehicleToRemove = vehicleData.findVehicleByPlate(plate);
        if (vehicleToRemove != null) {
            vehicleData.removeVehicle(vehicleToRemove);
            return "Vehículo removido con éxito";
        } else {
            return "No se removió el vehículo porque no existe en la base de datos";
        }
    }

    public String updateVehicle(Vehicle vehicle) {
        Vehicle existing = vehicleData.findVehicleByPlate(vehicle.getPlate());
        if (existing != null) {
            vehicleData.updateVehicle(existing, vehicle);
            return "Vehículo actualizado con éxito";
        } else {
            return "No se pudo actualizar el vehículo porque no existe";
        }

    }
}
