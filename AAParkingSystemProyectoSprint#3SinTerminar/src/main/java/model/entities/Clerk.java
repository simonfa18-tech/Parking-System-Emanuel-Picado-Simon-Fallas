/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author Lab01
 */

public class Clerk extends User {

    private int assignedParkingId;

    public Clerk(String username, String password, String name,
                 int age, String phone, String email,
                 int assignedParkingId) {

        super(username, password, name, age, phone, email, "CLERK");
        this.assignedParkingId = assignedParkingId;
    }

    public int getAssignedParkingId() {
        return assignedParkingId;
    }

    public void setAssignedParkingId(int assignedParkingId) {
        this.assignedParkingId = assignedParkingId;
    }
}