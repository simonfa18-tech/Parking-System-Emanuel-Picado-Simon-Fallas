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

    public Clerk(String username, String password, String name, int age, String phone, String email) {
        super(username, password, name, age, phone, email, "CLERK");
    }
}