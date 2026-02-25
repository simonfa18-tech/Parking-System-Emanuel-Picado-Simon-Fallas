/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.UserData;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;

/**
 *
 * @author emman
 */
public class UserController {

    private final UserData userData = new UserData();

    public User login(String username, String password) {
        return userData.login(username, password);
    }

    public String createClerk(String username, String password,
            String name, int age,
            String phone, String email,
            int parkingId) {

        if (userData.findByUsername(username) != null) {
            return "The user already exists";
        }

        Clerk clerk = new Clerk(username, password, name, age, phone, email, parkingId);
        userData.insertUser(clerk);

        return "Clerk created successfully";
    }

    public String createAdministrator(String username, String password, String name, int age, String phone, String email) {
        if (userData.findByUsername(username) != null) {
            return "The user already exists";
        }

        Administrator admin = new Administrator(username, password, name, age, phone, email, "administrador");
        userData.insertUser(admin);
        return "Administrator created successfully";
    }

    public ArrayList<User> getAllUsers() {
        return userData.getAllUsers();
    }

    public String deleteUser(String username) {
        User u = userData.findByUsername(username);
        if (u == null) {
            return "User not found";
        }

        userData.removeUser(u);
        return "User eliminated";
    }

    public User findByUsername(String username) {
        return userData.findByUsername(username);
    }

    public String updateUser(User updated) {
        User existing = userData.findByUsername(updated.getUsername());
        if (existing == null) {
            return "Usuario dont exist";
        }

        userData.updateUser(existing, updated);
        return "User update";
    }
}
