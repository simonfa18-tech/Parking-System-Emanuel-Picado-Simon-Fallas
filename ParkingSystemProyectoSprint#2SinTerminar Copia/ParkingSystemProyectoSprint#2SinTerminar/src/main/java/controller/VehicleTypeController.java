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
            return "Tipo de vehículo insertado con éxito";
        } else {
            return "No se insertó el tipo de vehículo porque ya existe";
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
            return "Tipo de vehículo removido con éxito";
        } else {
            return "No se removió el tipo de vehículo porque no existe";
        }
    }

    public String updateVehicleType(VehicleType vehicleType) {
        VehicleType existing = vehicleTypeData.findVehicleTypeById(vehicleType.getId());
        if (existing != null) {
            vehicleTypeData.updateVehicleType(existing, vehicleType);
            return "Tipo de vehículo actualizado con éxito";
        } else {
            return "No se pudo actualizar el tipo de vehículo porque no existe";
        }
    }
}
