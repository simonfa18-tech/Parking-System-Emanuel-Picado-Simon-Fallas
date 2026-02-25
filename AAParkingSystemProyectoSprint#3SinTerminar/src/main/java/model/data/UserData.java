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
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;

/**
 *
 * @author emman
 */
public class UserData {

    private final String FILE_PATH = "users.txt";
    private final ArrayList<User> users = new ArrayList<>();

    public UserData() {
        loadFromFile();

    }

    public void insertUser(User user) {
        users.add(user);
        saveToFile();
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null;
    }

    public void removeUser(User user) {
        users.remove(user);
        saveToFile();
    }

    public void updateUser(User existing, User updated) {
        existing.setName(updated.getName());
        existing.setAge(updated.getAge());
        existing.setPhone(updated.getPhone());
        existing.setEmail(updated.getEmail());
        existing.setPassword(updated.getPassword());

        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (User user : users) {

                if (user instanceof Administrator admin) {
                    writer.write(
                            "ADMIN;"
                            + admin.getUsername() + ";"
                            + admin.getPassword() + ";"
                            + admin.getName() + ";"
                            + admin.getAge() + ";"
                            + admin.getPhone() + ";"
                            + admin.getEmail() + ";"
                            + admin.getPosition()
                    );
                } else if (user instanceof Clerk clerk) {
                    writer.write(
                            "CLERK;"
                            + clerk.getUsername() + ";"
                            + clerk.getPassword() + ";"
                            + clerk.getName() + ";"
                            + clerk.getAge() + ";"
                            + clerk.getPhone() + ";"
                            + clerk.getEmail() + ";"
                            + clerk.getAssignedParkingId()
                    );
                }

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error guardando users.txt");
            e.printStackTrace();
        }
    }

    private void loadFromFile() {

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");

                String type = parts[0];
                String username = parts[1];
                String password = parts[2];
                String name = parts[3];
                int age = Integer.parseInt(parts[4]);
                String phone = parts[5];
                String email = parts[6];

                if (type.equals("ADMIN")) {
                    String role = parts[7];
                    users.add(new Administrator(
                            username, password, name, age, phone, email, role
                    ));
                } else if (type.equals("CLERK")) {

                    int parkingId = -1; // valor por defecto

                    if (parts.length >= 8) {
                        parkingId = Integer.parseInt(parts[7]);
                    }

                    users.add(new Clerk(
                            username,
                            password,
                            name,
                            age,
                            phone,
                            email,
                            parkingId
                    ));
                }
            }

        } catch (Exception e) {
            System.out.println("Error leyendo users.txt");
            e.printStackTrace();
        }
    }
}
