/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.entities.VehicleType;

/**
 *
 * @author emman
 */
public class VehicleTypeData {

    private static final String FILE_PATH = "vehicleTypes.txt";
    private static ArrayList<VehicleType> vehicleTypes = new ArrayList<>();

    public VehicleTypeData() {
        loadFromFile();
    }

    public void insertVehicleType(VehicleType vehicleType) {
        vehicleTypes.add(vehicleType);
        saveToFile();
    }

    public static ArrayList<VehicleType> getAllVehicleTypes() {
        return vehicleTypes;
    }

    public VehicleType findVehicleTypeById(int id) {
        for (VehicleType vt : vehicleTypes) {
            if (vt.getId() == id) {
                return vt;
            }
        }
        return null;
    }

    public void removeVehicleType(VehicleType vehicleType) {
        vehicleTypes.remove(vehicleType);
        saveToFile();
    }

    public void updateVehicleType(VehicleType existing, VehicleType updated) {
        existing.setId(updated.getId());
        existing.setDescription(updated.getDescription());
        existing.setNumberOfTires(updated.getNumberOfTires());
        existing.setFee(updated.getFee());
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (VehicleType vt : vehicleTypes) {
                writer.write(
                        vt.getId() + ";"
                        + vt.getDescription() + ";"
                        + vt.getNumberOfTires() + ";"
                        + vt.getFee()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error guardando vehicleTypes.txt");
            e.printStackTrace();
        }
    }

    private void loadFromFile() {

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        vehicleTypes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");

                int id = Integer.parseInt(parts[0]);
                String desc = parts[1];
                int tires = Integer.parseInt(parts[2]);
                double fee = Double.parseDouble(parts[3]);

                vehicleTypes.add(new VehicleType(id, desc, tires, fee));
            }

        } catch (Exception e) {
            System.out.println("Error leyendo vehicleTypes.txt");
            e.printStackTrace();
        }
    }
}
