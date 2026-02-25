/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ParkingLotController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Arrays;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;

public class ManagementParkingLotView extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnEdit, btnDelete, btnRefresh, btnSearch, btnShowAll;
    private JTextField txtSearch;

    private ParkingLotController controller;
    private ParkingLotView parkingLotView;

    private final String[] headers = {
        "ID",
        "Name",
        "Total",
        "Occupied",
        "Available",
        "♿ Total",
        "♿ Occupied",
        "♿ Available"
    };

    public ManagementParkingLotView(ParkingLotView parkingLotView, ParkingLotController controller) {
        super("Parking Lot Management", false, true, false, true);
        this.parkingLotView = parkingLotView;
        this.controller = controller;
        init();
        refreshTable();
    }

    private void init() {
        setSize(800, 300);
        setLocation(250, 150);
        setResizable(false);
        setVisible(true);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(230, 230, 240));

        JLabel header = new JLabel("Parking Lot Management", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        outerPanel.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = table.getColumnModel();
        int[] widths = {40, 180, 50, 50, 50, 60, 60, 60};
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(780, 180));
        outerPanel.add(scroll, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchPanel.setBackground(Color.WHITE);

        txtSearch = new JTextField(10);

        btnSearch = new JButton("Search by ID");
        btnSearch.addActionListener(e -> searchParking());

        btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(e -> refreshTable());

        searchPanel.add(new JLabel("Parking ID:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        outerPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        toolbar.setBackground(Color.WHITE);

        btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(60, 179, 113));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> editParking());

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteParking());

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

        for (ParkingLot p : controller.getAllParkingLots()) {
            int totalSpaces = p.getSpaces().length;
            int occupied = p.getVehicles().size();
            int available = totalSpaces - occupied;

            long disabilityTotal = Arrays.stream(p.getSpaces())
                    .filter(Space::isDisabilityAdaptation)
                    .count();

            long disabilityOccupied = p.getVehicles().stream()
                    .map(Vehicle::getSpace)
                    .filter(space -> space != null && space.isDisabilityAdaptation())
                    .count();

            long disabilityAvailable = disabilityTotal - disabilityOccupied;

            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                totalSpaces,
                occupied,
                available,
                disabilityTotal,
                disabilityOccupied,
                disabilityAvailable
            });
        }
    }

    private void searchParking() {
        String text = txtSearch.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a parking ID");
            return;
        }

        try {
            int id = Integer.parseInt(text);
            ParkingLot p = controller.findParkingLotById(id);

            model.setRowCount(0);

            if (p != null) {
                int totalSpaces = p.getSpaces().length;
                int occupied = p.getVehicles().size();
                int available = totalSpaces - occupied;

                long disabilityTotal = Arrays.stream(p.getSpaces())
                        .filter(Space::isDisabilityAdaptation)
                        .count();

                long disabilityOccupied = p.getVehicles().stream()
                        .map(Vehicle::getSpace)
                        .filter(space -> space != null && space.isDisabilityAdaptation())
                        .count();

                long disabilityAvailable = disabilityTotal - disabilityOccupied;

                model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    totalSpaces,
                    occupied,
                    available,
                    disabilityTotal,
                    disabilityOccupied,
                    disabilityAvailable
                });

            } else {
                JOptionPane.showMessageDialog(this, "Parking lot not found");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
        }
    }

    private void editParking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a parking lot");
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        ParkingLot parking = controller.findParkingLotById(id);
        parkingLotView.loadParkingForEdit(parking);
        dispose();
    }

    private void deleteParking() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a parking lot");
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        JOptionPane.showMessageDialog(this, controller.deleteParkingLot(id));
        refreshTable();
    }
}
