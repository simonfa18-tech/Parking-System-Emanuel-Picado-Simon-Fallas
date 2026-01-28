/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

/**
 *
 * @author Lab01
 */
public class ParkingLotData {

    private ArrayList<ParkingLot> parkingLots;
    static int parkingLotId = 0;

    public ParkingLotData() {
        parkingLots = new ArrayList<>();
    }

    public ParkingLot registerParkingLot(String name, int totalSpaces, int disabilitySpaces) {
        Space[] spaces = new Space[totalSpaces];
        for (int i = 0; i < totalSpaces; i++) {
            boolean isDisability = i < disabilitySpaces;
            spaces[i] = new Space(i + 1, isDisability, null);
        }
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(parkingLots.size() + 1);
        parkingLot.setName(name);
        parkingLot.setSpaces(spaces);
        parkingLot.setVehicles(new ArrayList<>());
        parkingLots.add(parkingLot);
        return parkingLot;
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        Space[] spaces = parkingLot.getSpaces();
        ArrayList<Vehicle> vehiclesInParkingLot = parkingLot.getVehicles();
        boolean hasDisabilityOwner = vehicle.getOwners().stream().anyMatch(Customer::isDisabilityPresented);
        int spaceId = -1;
        for (Space space : spaces) {
            if (!space.isSpaceTaken() && space.getVehicleType().getId() == vehicle.getType().getId()) {
                if (space.isDisabilityAdaptation() && !hasDisabilityOwner) {
                    continue;
                }
                vehiclesInParkingLot.add(vehicle);
                space.setSpaceTaken(true);
                vehicle.setSpace(space);
                spaceId = space.getId();
                break;
            }
        }
        return spaceId;
    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        ArrayList<Vehicle> vehiclesInParkingLot = parkingLot.getVehicles();
        if (vehiclesInParkingLot.remove(vehicle)) {
            if (vehicle.getSpace() != null) {
                vehicle.getSpace().setSpaceTaken(false);
                vehicle.setSpace(null);
            }
        }
    }

    public ParkingLot findParkingLotById(int id) {
        for (ParkingLot parkingLot : parkingLots) {
            if (parkingLot.getId() == id) {
                return parkingLot;
            }
        }
        return null;
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLots;
    }
}
