/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import java.awt.*;
import javax.swing.*;
import model.entities.Customer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerView extends JInternalFrame implements ActionListener {

    private JTextField textFieldId, textFieldName;
    private JCheckBox checkDisability;
    private JButton buttonSave, buttonCancel, buttonView;
    private boolean editMode = false;
    private CustomerController controller;
    private ManagementCustomerView managementCustomerView;

    public CustomerView(CustomerController controller) {
        super("Register of clients", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(500, 340);
        setLocation(220, 80);
        setResizable(false);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(200, 220, 240));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel header = new JLabel("Register of clients", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        outerPanel.add(header, BorderLayout.NORTH);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel labelId = new JLabel("Id");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(labelId, gbc);
        textFieldId = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(textFieldId, gbc);
        JLabel labelName = new JLabel("Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(labelName, gbc);
        textFieldName = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(textFieldName, gbc);
        checkDisability = new JCheckBox("Has a disability");
        checkDisability.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(checkDisability, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonSave = new JButton("Save");
        buttonSave.setBackground(new Color(60, 179, 113));
        buttonSave.setForeground(Color.WHITE);
        buttonSave.setFocusPainted(false);
        buttonSave.addActionListener(this);
        buttonView = new JButton("See clients");
        buttonView.setBackground(new Color(70, 130, 180));
        buttonView.setForeground(Color.WHITE);
        buttonView.setFocusPainted(false);
        buttonView.addActionListener(this);
        buttonCancel = new JButton("Exit");
        buttonCancel.setBackground(new Color(220, 20, 60));
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setFocusPainted(false);
        buttonCancel.addActionListener(e -> dispose());
        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonView);
        buttonPanel.add(buttonCancel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        outerPanel.add(panel, BorderLayout.CENTER);
        add(outerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSave) {
            saveCustomer();
        } else if (e.getSource() == buttonView) {
            openCustomerManagement();
        }
    }

    private void saveCustomer() {
        if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must complete all fields");
            return;
        }
        Customer customer = new Customer(textFieldId.getText(), textFieldName.getText(), checkDisability.isSelected());
        String result = editMode ? controller.updateCustomer(customer) : controller.insertCustomer(customer);
        JOptionPane.showMessageDialog(this, result);
        if (managementCustomerView != null) {
            managementCustomerView.refreshTable();
        }
        clearFields();
    }

    private void openCustomerManagement() {
        if (managementCustomerView == null || managementCustomerView.isClosed()) {
            managementCustomerView = new ManagementCustomerView(this, controller);
            JDesktopPane desktop = getDesktopPane();
            desktop.add(managementCustomerView);
        }
        managementCustomerView.setVisible(true);
        managementCustomerView.toFront();
    }

    public void loadCustomerForEdit(Customer customer) {
        setTitle("Edit Client");
        editMode = true;
        textFieldId.setText(customer.getId());
        textFieldId.setEditable(false);
        textFieldName.setText(customer.getName());
        checkDisability.setSelected(customer.isDisabilityPresented());
        buttonSave.setText("Update");
        toFront();
    }

    private void clearFields() {
        if (!editMode) {
            textFieldId.setText("");
        }
        textFieldName.setText("");
        checkDisability.setSelected(false);
        textFieldId.requestFocus();
    }
}