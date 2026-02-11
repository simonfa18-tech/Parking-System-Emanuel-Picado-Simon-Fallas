/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.data.ParkingLotData;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

/**
 *
 * @author Lab01
 */
public class ParkingLotController {

    private static final ParkingLotData parkingLotData = new ParkingLotData();

    public ParkingLot registerParkingLot(String name, int totalSpaces, int disabilitySpaces) {
        return parkingLotData.registerParkingLot(name, totalSpaces, disabilitySpaces);
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parking) {
        for (Space space : parking.getSpaces()) {
            if (!space.isSpaceTaken() && space.getVehicleType().getId() == vehicle.getType().getId()) {
                boolean hasDisabilityOwner = vehicle.getOwners().stream().anyMatch(Customer::isDisabilityPresented);
                if (space.isDisabilityAdaptation() && !hasDisabilityOwner) {
                    continue;
                }
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                parking.getVehicles().add(vehicle);
                return space.getId();
            }
        }
        return 0;
    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        if (vehicle.getSpace() != null) {
            vehicle.getSpace().setSpaceTaken(false);
            vehicle.setSpace(null);
        }
        parkingLot.getVehicles().remove(vehicle);
    }

    public ParkingLot findParkingLotById(int id) {
        return parkingLotData.findParkingLotById(id);
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotData.getAllParkingLots();
    }

    public double removeVehicleByPlateAndCalculateFee(String plate, ParkingLot parkingLot) {
        
        if (parkingLot == null || parkingLot.getVehicles().isEmpty()) {
            throw new IllegalArgumentException("No hay vehículos en este parqueo");
        }

 
        Vehicle vehicle = parkingLot.getVehicles()
                .stream()
                .filter(v -> v.getPlate().equalsIgnoreCase(plate))
                .findFirst()
                .orElse(null);

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehículo con placa " + plate + " no encontrado");
        }

   
        LocalDateTime exitTime = LocalDateTime.now();
        LocalDateTime entryTime = vehicle.getEntryTime();


        long seconds = Duration.between(entryTime, exitTime).getSeconds();
        long hours = (seconds / 60) + 1; 

        double total = hours * vehicle.getType().getFee();

        removeVehicleFromParkingLot(vehicle, parkingLot);
        vehicle.setExitTime(exitTime);

        return total;
    }

    public int reassignVehicleToDisabilitySpace(Vehicle vehicle, ParkingLot parkingLot) {
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && space.isDisabilityAdaptation() && space.getVehicleType().getId() == vehicle.getType().getId()) {
                if (vehicle.getSpace() != null) {
                    vehicle.getSpace().setSpaceTaken(false);
                }
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                if (!parkingLot.getVehicles().contains(vehicle)) {
                    parkingLot.getVehicles().add(vehicle);
                }
                return space.getId();
            }
        }
        if (!parkingLot.getVehicles().contains(vehicle)) {
            parkingLot.getVehicles().add(vehicle);
        }
        return -1;
    }

    public int assignVehicleToDisabilitySpace(Vehicle vehicle, ParkingLot parkingLot) {
        for (Space space : parkingLot.getSpaces()) {
            if (!space.isSpaceTaken() && space.isDisabilityAdaptation() && space.getVehicleType().getId() == vehicle.getType().getId()) {
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                parkingLot.getVehicles().add(vehicle);
                return space.getId();
            }
        }
        return -1;
    }
}
