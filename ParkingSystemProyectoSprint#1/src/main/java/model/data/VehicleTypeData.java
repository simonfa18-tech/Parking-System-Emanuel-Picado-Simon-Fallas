/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.VehicleType;

/**
 *
 * @author emman
 */
public class VehicleTypeData {

    static ArrayList<VehicleType> vehicleTypes = new ArrayList<>();

    public void insertVehicleType(VehicleType vehicleType) {
        vehicleTypes.add(vehicleType);
    }

    public static ArrayList<VehicleType> getAllVehicleTypes() {

       
        if (vehicleTypes.isEmpty()) {
            vehicleTypes.add(new VehicleType(1, "Liviano", 2, 500));
            vehicleTypes.add(new VehicleType(2, "Moto", 4, 200));
            vehicleTypes.add(new VehicleType(3, "Pesado", 8, 1000));
            vehicleTypes.add(new VehicleType(4, "Bici", 2, 100));
            vehicleTypes.add(new VehicleType(5, "otro", 4, 700));
        }
        return vehicleTypes;
    }

    public VehicleType findVehicleTypeById(int id) {
        VehicleType vehicleTypeToReturn = null;

        for (VehicleType vehicleType : vehicleTypes) {
            if (vehicleType.getId() == id) {
                vehicleTypeToReturn = vehicleType;
            }
        }
        return vehicleTypeToReturn;
    }

    public void removeVehicleType(VehicleType vehicleType) {
        vehicleTypes.remove(vehicleType);
    }

    public void updateVehicleType(VehicleType existing, VehicleType updated) {
        existing.setId(updated.getId());
        existing.setDescription(updated.getDescription());
        existing.setNumberOfTires(updated.getNumberOfTires());
        existing.setFee(updated.getFee());
    }
}
