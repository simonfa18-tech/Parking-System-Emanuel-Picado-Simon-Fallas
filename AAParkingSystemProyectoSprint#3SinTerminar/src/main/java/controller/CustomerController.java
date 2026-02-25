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
            return "Client successfully inserted";
        } else {
            return "The client was not inserted because it already exists in the database.";
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
            return "Client successfully removed";
        } else {
            return "The client was not removed because they do not exist in the database.";
        }
    }

    public String updateCustomer(Customer customer) {
        Customer existing = customerData.findCustomerById(customer.getId());
        if (existing != null) {
            customerData.updateCustomer(existing, customer);
            return "Client successfully updated";
        } else {
            return "The client could not be updated because it does not exist.";
        }

    }
}
