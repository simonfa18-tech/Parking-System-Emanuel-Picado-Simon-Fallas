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

    public String createClerk(String username, String password, String name, int age, String phone, String email) {
        if (userData.findByUsername(username) != null) {
            return "El usuario ya existe";
        }

        Clerk clerk = new Clerk(username, password, name, age, phone, email);
        userData.insertUser(clerk);
        return "Clerk creado correctamente";
    }

    public String createAdministrator(String username, String password, String name, int age, String phone, String email) {
        if (userData.findByUsername(username) != null) {
            return "El usuario ya existe";
        }

        Administrator admin = new Administrator(username, password, name, age, phone, email, "administrador");
        userData.insertUser(admin);
        return "Administrador creado correctamente";
    }

    public ArrayList<User> getAllUsers() {
        return userData.getAllUsers();
    }

    public String deleteUser(String username) {
        User u = userData.findByUsername(username);
        if (u == null) {
            return "Usuario no encontrado";
        }

        userData.removeUser(u);
        return "Usuario eliminado";
    }

    public User findByUsername(String username) {
        return userData.findByUsername(username);
    }

    public String updateUser(User updated) {
        User existing = userData.findByUsername(updated.getUsername());
        if (existing == null) {
            return "Usuario no existe";
        }

        userData.updateUser(existing, updated);
        return "Usuario actualizado";
    }
}
