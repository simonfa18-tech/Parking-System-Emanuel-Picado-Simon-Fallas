/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import java.io.Serializable;

/**
 *
 * @author Lab01
 */
public class VehicleType implements Serializable {

    private int id;
    private String description;
    private int numberOfTires;
    private double fee;

    public VehicleType() {
    }

    public VehicleType(int id, String description, int numberOfTires, double fee) {
        this.id = id;
        this.description = description;
        this.numberOfTires = numberOfTires;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfTires() {
        return numberOfTires;
    }

    public void setNumberOfTires(int numberOfTires) {
        this.numberOfTires = numberOfTires;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return description + " Tarifa: " + fee;
    }
}
