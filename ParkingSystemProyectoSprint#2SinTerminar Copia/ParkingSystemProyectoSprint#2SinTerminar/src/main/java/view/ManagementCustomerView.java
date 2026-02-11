/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import model.entities.Customer;

public class ManagementCustomerView extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnEdit, btnDelete, btnRefresh;
    private CustomerController controller;
    private CustomerView customerView;
    private final String[] headers = {"ID", "Name", "Disability"};

    public ManagementCustomerView(CustomerView window, CustomerController controller) {
        super("Customer Management", false, true, false, true);
        this.customerView = window;
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(600, 400);
        setLocation(250, 120);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(230, 230, 240));
        JLabel header = new JLabel("Customer Management", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        outerPanel.add(header, BorderLayout.NORTH);
        model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(new Color(200, 200, 200));
        JScrollPane scroll = new JScrollPane(table);
        outerPanel.add(scroll, BorderLayout.CENTER);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        toolbar.setBackground(Color.WHITE);
        btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(60, 179, 113));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteCustomer());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> refreshTable());
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        outerPanel.add(toolbar, BorderLayout.SOUTH);
        add(outerPanel);
        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        ArrayList<Customer> customers = controller.getAllCustomers();
        for (Customer c : customers) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.isDisabilityPresented() ? "Yes" : "No"});
        }
    }

    private void editCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer");
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        Customer customer = controller.findCustomerById(id);
        customerView.loadCustomerForEdit(customer);
        dispose();
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer");
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        JOptionPane.showMessageDialog(this, controller.removeCustomer(id));
        refreshTable();
    }
}