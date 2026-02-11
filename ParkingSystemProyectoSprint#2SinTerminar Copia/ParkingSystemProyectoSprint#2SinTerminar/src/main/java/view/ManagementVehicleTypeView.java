/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.VehicleTypeController;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.entities.VehicleType;

public class ManagementVehicleTypeView extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private final VehicleTypeController controller;
    private final VehicleTypeView form;
    private final String[] headers = {"ID", "Description", "Tires", "Fee"};
    private JButton btnEdit, btnDelete, btnRefresh;

    public ManagementVehicleTypeView(VehicleTypeView form, VehicleTypeController controller) {
        super("Vehicle Type Management", false, true, false, true);
        this.form = form;
        this.controller = controller;
        init();
        refreshTable();
    }

    private void init() {
        setSize(600, 380);
        setLocation(260, 120);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(230, 230, 240));
        JLabel header = new JLabel("Vehicle Type Management", SwingConstants.CENTER);
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
        btnEdit.addActionListener(e -> edit());
        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> delete());
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
    }

    public void refreshTable() {
        model.setRowCount(0);
        ArrayList<VehicleType> list = controller.getAllVehicleTypes();
        for (VehicleType vt : list) {
            model.addRow(new Object[]{vt.getId(), vt.getDescription(), vt.getNumberOfTires(), vt.getFee()});
        }
    }

    private void edit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle type");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        VehicleType vt = controller.findVehicleTypeById(id);
        form.loadForEdit(vt);
        setVisible(false);
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle type");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        JOptionPane.showMessageDialog(this, controller.removeVehicleType(id));
        refreshTable();
    }
}
