package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import model.entities.*;

public class VehicleView extends JInternalFrame implements ActionListener {

    private final VehicleController vehicleController;
    private final CustomerController customerController;
    private final ParkingLotController parkingLotController;
    private final VehicleTypeController vehicleTypeController;
    private ManagementVehicleView managementVehicleView;
    private JTextField txtPlate, txtBrand, txtModel, txtColor;
    private JComboBox<VehicleType> comboVehicleType;
    private JComboBox<ParkingLot> comboParkingLot;
    private JButton btnAddOwner, btnSave, btnViewVehicles, btnExit;
    private ArrayList<Customer> owners = new ArrayList<>();
    private boolean editMode = false;
    private Vehicle vehicleEditing = null;

    public VehicleView(VehicleController vehicleController, CustomerController customerController,
            ParkingLotController parkingLotController, VehicleTypeController vehicleTypeController) {
        super("Vehicle Registration", false, true, false, true);
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.parkingLotController = parkingLotController;
        this.vehicleTypeController = vehicleTypeController;
        init();
    }

    private void init() {
        setSize(550, 450);
        setLocation(220, 80);
        setVisible(true);
        setResizable(false);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(200, 220, 240));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Vehicle Registration", SwingConstants.CENTER);
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
        formPanel.add(new JLabel("Plate:"), gbc);
        txtPlate = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtPlate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Brand:"), gbc);
        txtBrand = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtBrand, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Model:"), gbc);
        txtModel = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtModel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Color:"), gbc);
        txtColor = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtColor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Type:"), gbc);
        comboVehicleType = new JComboBox<>();
        loadVehicleTypes();
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(comboVehicleType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Parking Lot:"), gbc);
        comboParkingLot = new JComboBox<>();
        loadParkingLots();
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(comboParkingLot, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnAddOwner = new JButton("Add Owner");
        btnAddOwner.addActionListener(this);

        btnSave = new JButton("Save & Park");
        btnSave.setBackground(new Color(60, 179, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this);

        btnViewVehicles = new JButton("View Vehicles");
        btnViewVehicles.setBackground(new Color(70, 130, 180));
        btnViewVehicles.setForeground(Color.WHITE);
        btnViewVehicles.setFocusPainted(false);
        btnViewVehicles.addActionListener(this);

        btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> dispose());

        buttonPanel.add(btnAddOwner);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnViewVehicles);
        buttonPanel.add(btnExit);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        outerPanel.add(formPanel, BorderLayout.CENTER);
        add(outerPanel);
    }

    private void loadVehicleTypes() {
        comboVehicleType.removeAllItems();
        for (VehicleType type : vehicleTypeController.getAllVehicleTypes()) {
            comboVehicleType.addItem(type);
        }
    }

    private void loadParkingLots() {
        comboParkingLot.removeAllItems();
        for (ParkingLot p : parkingLotController.getAllParkingLots()) {
            comboParkingLot.addItem(p);
        }
    }

    public void loadVehicleForEdit(Vehicle vehicle) {
        setTitle("Edit Vehicle");
        editMode = true;
        vehicleEditing = vehicle;
        txtPlate.setText(vehicle.getPlate());
        txtPlate.setEditable(false);
        txtColor.setText(vehicle.getColor());
        txtBrand.setText(vehicle.getBrand());
        txtModel.setText(vehicle.getModel());
        comboVehicleType.setSelectedItem(vehicle.getVehicleType());
        owners.clear();
        owners.addAll(vehicle.getOwners());
        btnSave.setText("Update");
    }

    private void clear() {
        txtPlate.setText("");
        txtPlate.setEditable(true);
        txtColor.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        comboVehicleType.setSelectedIndex(-1);
        owners = new ArrayList<>();
        editMode = false;
        vehicleEditing = null;
        btnSave.setText("Save & Park");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddOwner) {
            addOwner();
        }
        if (e.getSource() == btnSave) {
            saveVehicle();
        }
        if (e.getSource() == btnViewVehicles) {
            openVehicleManagement();
        }
    }

    private void addOwner() {
        String id = JOptionPane.showInputDialog(this, "Customer ID");
        if (id == null || id.isEmpty()) {
            return;
        }
        Customer c = customerController.findCustomerById(id);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Customer not found");
            return;
        }
        owners.add(c);
        JOptionPane.showMessageDialog(this, "Owner added successfully");
    }

    private void saveVehicle() {
        if (owners.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must add at least one owner");
            return;
        }
        String message;
        if (editMode) {
            vehicleEditing.setColor(txtColor.getText());
            vehicleEditing.setBrand(txtBrand.getText());
            vehicleEditing.setModel(txtModel.getText());
            vehicleEditing.setOwners(owners);
            vehicleEditing.setVehicleType((VehicleType) comboVehicleType.getSelectedItem());
            ParkingLot parking = (ParkingLot) comboParkingLot.getSelectedItem();
            message = vehicleController.updateVehicle(vehicleEditing, parking);
        } else {
            Vehicle vehicle = new Vehicle(txtPlate.getText(), txtColor.getText(), txtBrand.getText(), txtModel.getText(), owners, (VehicleType) comboVehicleType.getSelectedItem());
            ParkingLot parking = (ParkingLot) comboParkingLot.getSelectedItem();
            vehicleController.insertVehicle(vehicle, parking);
            parkingLotController.registerVehicleInParkingLot(vehicle, parking);
            message = "Vehicle registered successfully";
        }
        JOptionPane.showMessageDialog(this, message);
        clear();
        if (managementVehicleView != null) {
            managementVehicleView.refreshTable();
        }
    }

    private void openVehicleManagement() {
        if (managementVehicleView == null || managementVehicleView.isClosed()) {
            managementVehicleView = new ManagementVehicleView(parkingLotController, customerController, vehicleController, this);
            JDesktopPane desktop = getDesktopPane();
            desktop.add(managementVehicleView);
        }
        managementVehicleView.setVisible(true);
        managementVehicleView.toFront();
    }

    public void setManagementVehicleView(ManagementVehicleView managementVehicleView) {
        this.managementVehicleView = managementVehicleView;
    }
}
