package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import javax.swing.*;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ManagementVehicleView extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton btnEdit, btnDelete, btnAddOwner, btnRefresh;
    private ParkingLotController parkingController;
    private CustomerController customerController;
    private VehicleController vehicleController;
    private VehicleView vehicleView;
    private final String[] headers = {"Parking Lot", "Plate", "Type", "Owners"};

    public ManagementVehicleView(ParkingLotController parkingController, CustomerController customerController, VehicleController vehicleController, VehicleView vehicleView) {
        super("Vehicle Management", false, true, false, true);
        this.parkingController = parkingController;
        this.customerController = customerController;
        this.vehicleController = vehicleController;
        this.vehicleView = vehicleView;
        init();
        loadTable();
    }

    private void init() {
        setSize(750, 420);
        setLocation(220, 110);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(230, 230, 240));
        JLabel header = new JLabel("Vehicle Management", SwingConstants.CENTER);
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
        btnEdit.addActionListener(e -> editVehicle());
        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteVehicle());
        btnAddOwner = new JButton("Add Owner");
        btnAddOwner.setBackground(new Color(255, 165, 0));
        btnAddOwner.setForeground(Color.WHITE);
        btnAddOwner.setFocusPainted(false);
        btnAddOwner.addActionListener(e -> addOwner());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> refreshTable());
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnAddOwner);
        toolbar.add(btnRefresh);
        outerPanel.add(toolbar, BorderLayout.SOUTH);
        add(outerPanel);
    }

    private void loadTable() {
        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (ParkingLot parking : parkingController.getAllParkingLots()) {
            for (Vehicle vehicle : parking.getVehicles()) {
                model.addRow(new Object[]{parking.getName(), vehicle.getPlate(), vehicle.getVehicleType().getDescription(), ownersAsText(vehicle.getOwners())});
            }
        }
    }

    private String ownersAsText(ArrayList<Customer> customers) {
        StringBuilder text = new StringBuilder();
        for (Customer c : customers) {
            text.append(c.getName());
            if (c.isDisabilityPresented()) {
                text.append(" (D)");
            }
            text.append(", ");
        }
        if (text.length() > 0) {
            text.setLength(text.length() - 2);
        }
        return text.toString();
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle");
            return;
        }
        String parkingName = table.getValueAt(row, 0).toString();
        String plate = table.getValueAt(row, 1).toString();
        for (ParkingLot parking : parkingController.getAllParkingLots()) {
            if (parking.getName().equals(parkingName)) {
                for (Vehicle v : parking.getVehicles()) {
                    if (v.getPlate().equals(plate)) {
                        parkingController.removeVehicleFromParkingLot(v, parking);
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Vehicle deleted");
                        return;
                    }
                }
            }
        }
    }

    private void editVehicle() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle");
            return;
        }
        String plate = table.getValueAt(row, 1).toString();
        Vehicle vehicleFound = null;
        for (ParkingLot parking : parkingController.getAllParkingLots()) {
            for (Vehicle v : parking.getVehicles()) {
                if (v.getPlate().equalsIgnoreCase(plate)) {
                    vehicleFound = v;
                    break;
                }
            }
            if (vehicleFound != null) {
                break;
            }
        }
        if (vehicleFound == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found");
            return;
        }
        vehicleView.loadVehicleForEdit(vehicleFound);
        setVisible(false);
        try {
            vehicleView.setSelected(true);
            vehicleView.toFront();
        } catch (Exception ignored) {
        }
        vehicleView.setManagementVehicleView(this);
    }

    private void addOwner() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a vehicle");
            return;
        }
        String plate = table.getValueAt(row, 1).toString();
        Vehicle vehicleFound = null;
        ParkingLot parkingFound = null;
        outer:
        for (ParkingLot parking : parkingController.getAllParkingLots()) {
            for (Vehicle v : parking.getVehicles()) {
                if (v.getPlate().equals(plate)) {
                    vehicleFound = v;
                    parkingFound = parking;
                    break outer;
                }
            }
        }
        if (vehicleFound == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found");
            return;
        }
        String id = JOptionPane.showInputDialog(this, "Customer ID");
        if (id == null || id.isEmpty()) {
            return;
        }
        Customer customer = customerController.findCustomerById(id);
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer does not exist");
            return;
        }
        if (vehicleFound.getOwners().stream().anyMatch(c -> c.getId().equals(id))) {
            JOptionPane.showMessageDialog(this, "Customer is already an owner of this vehicle");
            return;
        }
        vehicleFound.getOwners().add(customer);
        JOptionPane.showMessageDialog(this, "Owner added successfully");
        refreshTable();
    }
}
