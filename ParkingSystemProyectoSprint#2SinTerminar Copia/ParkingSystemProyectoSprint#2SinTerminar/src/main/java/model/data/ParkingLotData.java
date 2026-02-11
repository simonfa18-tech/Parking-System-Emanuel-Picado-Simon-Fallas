/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.*;
import java.util.ArrayList;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

public class ParkingLotData {

    private ArrayList<ParkingLot> parkingLots;
    private final String FILE = "parkingLots.dat";
    private VehicleData vehicleData;

    public ParkingLotData(VehicleData vehicleData) {
        this.vehicleData = vehicleData;
        parkingLots = new ArrayList<>();
        loadFromFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(parkingLots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(FILE);
        if (!file.exists()) {
            parkingLots = new ArrayList<>();
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            parkingLots = (ArrayList<ParkingLot>) in.readObject();
            for (ParkingLot p : parkingLots) {
                ArrayList<Vehicle> fixedVehicles = new ArrayList<>();
                for (Vehicle vFromFile : p.getVehicles()) {
                    Vehicle realVehicle = vehicleData.findVehicleByPlate(vFromFile.getPlate(), p);
                    if (realVehicle == null) {
                        vehicleData.insertVehicle(vFromFile, p);
                        realVehicle = vFromFile;
                    }
                    if (vFromFile.getSpace() != null) {
                        int spaceId = vFromFile.getSpace().getId();
                        for (Space s : p.getSpaces()) {
                            if (s.getId() == spaceId) {
                                s.setSpaceTaken(true);
                                realVehicle.setSpace(s);
                                break;
                            }
                        }
                    }
                    fixedVehicles.add(realVehicle);
                }
                p.setVehicles(fixedVehicles);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            parkingLots = new ArrayList<>();
        }
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
        saveToFile();
        return parkingLot;
    }

    public int registerVehicleInParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        Vehicle realVehicle = vehicleData.findVehicleByPlate(vehicle.getPlate(), parkingLot);
        if (realVehicle == null) {
            vehicleData.insertVehicle(vehicle, parkingLot);
            realVehicle = vehicle;
        }
        Space[] spaces = parkingLot.getSpaces();
        ArrayList<Vehicle> vehiclesInParkingLot = parkingLot.getVehicles();
        boolean hasDisabilityOwner = realVehicle.getOwners().stream().anyMatch(Customer::isDisabilityPresented);
        int spaceId = -1;
        for (Space space : spaces) {
            if (!space.isSpaceTaken() && space.getVehicleType() != null && space.getVehicleType().getId() == realVehicle.getVehicleType().getId()) {
                if (space.isDisabilityAdaptation() && !hasDisabilityOwner) {
                    continue;
                }
                vehiclesInParkingLot.add(realVehicle);
                space.setSpaceTaken(true);
                realVehicle.setSpace(space);
                spaceId = space.getId();
                break;
            }
        }
        saveToFile();
        return spaceId;
    }

    public void removeVehicleFromParkingLot(Vehicle vehicle, ParkingLot parkingLot) {
        Vehicle realVehicle = vehicleData.findVehicleByPlate(vehicle.getPlate(), parkingLot);
        if (parkingLot.getVehicles().remove(realVehicle)) {
            if (realVehicle.getSpace() != null) {
                realVehicle.getSpace().setSpaceTaken(false);
                realVehicle.setSpace(null);
            }
        }
        saveToFile();
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

    public void updateParkingLot(ParkingLot existing, String name, int totalSpaces, int disabilitySpaces) {
        existing.setName(name);
        Space[] newSpaces = new Space[totalSpaces];
        for (int i = 0; i < totalSpaces; i++) {
            boolean isDisability = i < disabilitySpaces;
            if (i < existing.getSpaces().length) {
                newSpaces[i] = existing.getSpaces()[i];
                newSpaces[i].setDisabilityAdaptation(isDisability);
            } else {
                newSpaces[i] = new Space(i + 1, isDisability, null);
            }
        }
        existing.setSpaces(newSpaces);
        saveToFile();
    }

    public void deleteParkingLot(ParkingLot parkingLot) {
        parkingLots.remove(parkingLot);
        saveToFile();
    }

    public void persist() {
        saveToFile();
    }
}
