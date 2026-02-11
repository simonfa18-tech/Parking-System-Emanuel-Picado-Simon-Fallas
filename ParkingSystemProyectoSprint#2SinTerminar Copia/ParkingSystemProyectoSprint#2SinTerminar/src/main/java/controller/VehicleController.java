/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.VehicleData;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

public class VehicleController {

    private final VehicleData vehicleData;
    private final ParkingLotController parkingController;

    public VehicleController(ParkingLotController parkingController) {
        this.parkingController = parkingController;
        this.vehicleData = new VehicleData();
    }

    public String insertVehicle(Vehicle vehicle, ParkingLot parkingLot) {
        if (vehicleData.findVehicleByPlate(vehicle.getPlate(), parkingLot) == null) {
            vehicleData.insertVehicle(vehicle, parkingLot);
            parkingController.persist();
            return "Vehicle inserted successfully";
        } else {
            return "Vehicle not inserted because it already exists in this parking lot";
        }
    }

    public ArrayList<Vehicle> getAllVehicles(ParkingLot parkingLot) {
        return vehicleData.getAllVehicles(parkingLot);
    }

    public Vehicle findVehicleByPlate(String plate, ParkingLot parkingLot) {
        return vehicleData.findVehicleByPlate(plate, parkingLot);
    }

    public String removeVehicle(String plate, ParkingLot parkingLot) {
        Vehicle vehicleToRemove = vehicleData.findVehicleByPlate(plate, parkingLot);
        if (vehicleToRemove != null) {
            vehicleData.removeVehicle(vehicleToRemove, parkingLot);
            parkingController.persist();
            return "Vehicle removed successfully";
        } else {
            return "Vehicle not removed because it does not exist in this parking lot";
        }
    }

    public String updateVehicle(Vehicle updatedVehicle, ParkingLot parkingLot) {
        Vehicle existing = vehicleData.findVehicleByPlate(updatedVehicle.getPlate(), parkingLot);
        if (existing == null) {
            return "Vehicle could not be updated because it does not exist";
        }
        Space oldSpace = existing.getSpace();
        vehicleData.updateVehicle(existing, updatedVehicle);
        if (oldSpace != null) {
            boolean needsDisability = existing.getOwners().stream().anyMatch(o -> o.isDisabilityPresented());
            if (needsDisability && (existing.getSpace() == null || !existing.getSpace().isDisabilityAdaptation())) {
                int newSpace = parkingController.reassignVehicleToDisabilitySpace(existing, parkingLot);
                if (newSpace == -1) {
                    return "Vehicle updated, but no disability-adapted space available";
                }
            }
        }
        parkingController.persist();
        return "Vehicle updated successfully";
    }
}
