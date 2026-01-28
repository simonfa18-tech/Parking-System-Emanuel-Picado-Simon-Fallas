/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Customer;

/**
 *
 * @author Lab01
 */
public class CustomerData {

    ArrayList<Customer> Customers = new ArrayList<>();

    public void insertCustomer(Customer customer) {
        Customers.add(customer);
    }

    public ArrayList<Customer> getAllCustomers() {
        return Customers;
    }

    public Customer findCustomerById(String id) {
        Customer customerToReturn = null;
        for (Customer customer : Customers) {
            if (customer.getId().equals(id)) {
                customerToReturn = customer;
            }
        }
        return customerToReturn;

    }

    public void removeCustomer(Customer customer) {
        Customers.remove(customer);
    }

    public void updateCustomer(Customer existing, Customer updated) {
        existing.setId(updated.getId());
        existing.setName(updated.getName());

    }
}
