/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.CustomerData;
import model.entities.Customer;

/**
 *
 * @author Lab01
 */
public class CustomerController {

    private final CustomerData customerData = new CustomerData();

    public String insertCustomer(Customer customer) {
        if (customerData.findCustomerById(customer.getId()) == null) {
            customerData.insertCustomer(customer);
            return "Cliente insertado con éxito";
        } else {
            return "No se insertó el cliente porque este ya existe en la base de datos";
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        return customerData.getAllCustomers();
    }

    public Customer findCustomerById(String id) {
        return customerData.findCustomerById(id);
    }

    public String removeCustomer(String id) {
        Customer customerToRemove = customerData.findCustomerById(id);
        if (customerToRemove != null) {
            customerData.removeCustomer(customerToRemove);
            return "Cliente removido con éxito";
        } else {
            return "No se removió el cliente porque este no existe en la base de datos";
        }
    }

    public String updateCustomer(Customer customer) {
        Customer existing = customerData.findCustomerById(customer.getId());
        if (existing != null) {
            customerData.updateCustomer(existing, customer);
            return "Cliente actualizado con éxito";
        } else {
            return "No se pudo actualizar el cliente porque no existe";
        }

    }
}
