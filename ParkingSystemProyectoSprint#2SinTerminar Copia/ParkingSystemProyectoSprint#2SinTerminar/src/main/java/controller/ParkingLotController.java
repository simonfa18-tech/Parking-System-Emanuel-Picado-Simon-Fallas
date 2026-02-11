/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.ParkingLotData;
import model.data.VehicleData;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

public class ParkingLotController {

    private ParkingLotData parkingLotData;
    private VehicleData vehicleData;

    public ParkingLotController() {
        vehicleData = new VehicleData();
        parkingLotData = new ParkingLotData(vehicleData);
    }

    public ParkingLot registerParkingLot(String name, int totalSpaces, int disabilitySpaces) {
        return parkingLotData.registerParkingLot(name, totalSpaces, disabilitySpaces);
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parking) {
        Vehicle existing = vehicleData.findVehicleByPlate(vehicle.getPlate(), parking);
        if (existing != null) {
            return -1;
        }
        for (Space space : parking.getSpaces()) {
            if (!space.isSpaceTaken() && space.getVehicleType().getId() == vehicle.getVehicleType().getId()) {
                boolean hasDisabilityOwner = vehicle.getOwners().stream().anyMatch(Customer::isDisabilityPresented);
                if (space.isDisabilityAdaptation() && !hasDisabilityOwner) {
                    continue;
                }
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                vehicleData.insertVehicle(vehicle, parking);
                parkingLotData.persist();
                return space.getId();
            }
        }
        return -1;
    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        if (vehicle.getSpace() != null) {
            vehicle.getSpace().setSpaceTaken(false);
            vehicle.setSpace(null);
        }
        vehicleData.removeVehicle(vehicle, parkingLot);
        parkingLotData.persist();
    }

    public ParkingLot findParkingLotById(int id) {
        return parkingLotData.findParkingLotById(id);
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotData.getAllParkingLots();
    }

    public double removeVehicleByPlateAndCalculateFee(String plate, ParkingLot parkingLot) {
        if (parkingLot == null || parkingLot.getVehicles().isEmpty()) {
            throw new IllegalArgumentException("No vehicles in this parking lot");
        }
        Vehicle vehicle = vehicleData.findVehicleByPlate(plate, parkingLot);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle with plate " + plate + " not found");
        }
        LocalDateTime exitTime = LocalDateTime.now();
        LocalDateTime entryTime = vehicle.getEntryTime();
        if (entryTime == null) {
            throw new IllegalStateException("Vehicle entry time was not set");
        }
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes <= 0) {
            minutes = 1;
        }
        double total = minutes * vehicle.getVehicleType().getFee();
        removeVehicleFromParkingLot(vehicle, parkingLot);
        vehicle.setExitTime(exitTime);
        parkingLotData.persist();
        return total;
    }
    

    public int reassignVehicleToDisabilitySpace(Vehicle vehicle, ParkingLot parkingLot) {
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && space.isDisabilityAdaptation() && space.getVehicleType().getId() == vehicle.getVehicleType().getId()) {
                if (vehicle.getSpace() != null) {
                    vehicle.getSpace().setSpaceTaken(false);
                }
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                if (!parkingLot.getVehicles().contains(vehicle)) {
                    vehicleData.insertVehicle(vehicle, parkingLot);
                }
                parkingLotData.persist();
                return space.getId();
            }
        }
        if (!parkingLot.getVehicles().contains(vehicle)) {
            vehicleData.insertVehicle(vehicle, parkingLot);
        }
        parkingLotData.persist();
        return -1;
    }

    public int assignVehicleToDisabilitySpace(Vehicle vehicle, ParkingLot parkingLot) {
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && space.isDisabilityAdaptation() && space.getVehicleType().getId() == vehicle.getVehicleType().getId()) {
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                vehicleData.insertVehicle(vehicle, parkingLot);
                parkingLotData.persist();
                return space.getId();
            }
        }
        return -1;
    }

    public String updateParkingLot(int id, String name, int totalSpaces, int disabilitySpaces) {
        ParkingLot existing = parkingLotData.findParkingLotById(id);
        if (existing == null) {
            return "No se encontró el parqueo";
        }
        parkingLotData.updateParkingLot(existing, name, totalSpaces, disabilitySpaces);
        return "Parqueo actualizado correctamente";
    }

    public String deleteParkingLot(int id) {
        ParkingLot existing = parkingLotData.findParkingLotById(id);
        if (existing == null) {
            return "No se encontró el parqueo";
        }
        parkingLotData.deleteParkingLot(existing);
        return "Parqueo eliminado correctamente";
    }
    public void persist() {
        parkingLotData.persist();
    }
}
