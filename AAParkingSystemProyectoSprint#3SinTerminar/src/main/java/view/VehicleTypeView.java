/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.VehicleTypeController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.entities.VehicleType;

public class VehicleTypeView extends JInternalFrame implements ActionListener {

    private final VehicleTypeController controller;
    private JTextField txtId, txtDescription, txtTires, txtFee;
    private JButton btnSave, btnView, btnExit;
    private boolean editMode = false;
    private ManagementVehicleTypeView managementView;

    public VehicleTypeView(VehicleTypeController controller) {
        super("Vehicle Types", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(420, 340);
        setLocation(240, 90);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(230, 230, 240));
        JLabel header = new JLabel("Vehicle Types", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        outerPanel.add(header, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        txtDescription = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtDescription, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Tires:"), gbc);
        txtTires = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtTires, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Fee:"), gbc);
        txtFee = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtFee, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(60, 179, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this);
        btnView = new JButton("View Types");
        btnView.setBackground(new Color(70, 130, 180));
        btnView.setForeground(Color.WHITE);
        btnView.setFocusPainted(false);
        btnView.addActionListener(this);
        btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> dispose());
        buttonPanel.add(btnSave);
        buttonPanel.add(btnView);
        buttonPanel.add(btnExit);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        outerPanel.add(formPanel, BorderLayout.CENTER);
        add(outerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            saveVehicleType();
        }
        if (e.getSource() == btnView) {
            openManagement();
        }
    }

    private void saveVehicleType() {
        try {
            VehicleType vt = new VehicleType(Integer.parseInt(txtId.getText()), txtDescription.getText(), Integer.parseInt(txtTires.getText()), Double.parseDouble(txtFee.getText()));
            String msg = editMode ? controller.updateVehicleType(vt) : controller.insertVehicleType(vt);
            JOptionPane.showMessageDialog(this, msg);
            if (managementView != null) {
                managementView.refreshTable();
            }
            clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data");
        }
    }

    private void openManagement() {
        if (managementView == null || managementView.isClosed()) {
            managementView = new ManagementVehicleTypeView(this, controller);
            getDesktopPane().add(managementView);
        }
        managementView.setVisible(true);
        managementView.toFront();
    }

    public void loadForEdit(VehicleType vt) {
        editMode = true;
        setTitle("Edit Vehicle Type");
        txtId.setText(String.valueOf(vt.getId()));
        txtId.setEditable(false);
        txtDescription.setText(vt.getDescription());
        txtTires.setText(String.valueOf(vt.getNumberOfTires()));
        txtFee.setText(String.valueOf(vt.getFee()));
        btnSave.setText("Update");
    }

    private void clear() {
        txtId.setText("");
        txtId.setEditable(true);
        txtDescription.setText("");
        txtTires.setText("");
        txtFee.setText("");
        btnSave.setText("Save");
        editMode = false;
    }
}
