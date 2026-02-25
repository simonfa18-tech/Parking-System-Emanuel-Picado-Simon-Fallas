/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Lab01
 */
public class Vehicle implements Serializable {

    private String plate;
    private String color;
    private String brand;
    private String model;
    private ArrayList<Customer> customers;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private VehicleType vehicleType;
    private Space space;

    public Vehicle() {
        customers = new ArrayList<>();
        entryTime = LocalDateTime.now();
    }

    public Vehicle(String plate, String color, String brand, String model, ArrayList<Customer> customers, VehicleType vehicleType) {
        this.plate = plate;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.customers = customers;
        this.vehicleType = vehicleType;
        this.entryTime = LocalDateTime.now();
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<Customer> getOwners() {
        return customers;
    }

    public void setOwners(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle{" + "plate='" + plate + '\'' + ", color='" + color + '\'' + ", brand='" + brand + '\'' + ", model='" + model + '\'' + ", owners=" + customers + ", entryTime=" + entryTime + ", vehicleType=" + vehicleType + ", space=" + space + '}';
    }
}
