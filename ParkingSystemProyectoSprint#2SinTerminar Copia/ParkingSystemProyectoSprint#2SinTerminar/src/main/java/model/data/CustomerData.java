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
import model.entities.Customer;

/**
 *
 * @author Lab01
 */
public class CustomerData {

    private final ArrayList<Customer> customers = new ArrayList<>();
    private final String FILE_PATH = "customers.txt";

    public CustomerData() {
        loadFromFile();
    }

    public void insertCustomer(Customer customer) {
        customers.add(customer);
        System.out.println(new File(FILE_PATH).getAbsolutePath());
        saveToFile();
    }

    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        saveToFile();
    }

    public void updateCustomer(Customer existing, Customer updated) {
        existing.setName(updated.getName());
        existing.setDisabilityPresented(updated.isDisabilityPresented());
        saveToFile();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Customer c : customers) {
                writer.write(c.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando customers: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length == 3) {
                    String id = parts[0];
                    String name = parts[1];
                    boolean disability = Boolean.parseBoolean(parts[2]);

                    customers.add(new Customer(id, name, disability));
                }
            }

        } catch (IOException e) {
            System.out.println("Error cargando customers: " + e.getMessage());
        }
    }
}
