package view;

import controller.ParkingLotController;
import controller.SpaceController;
import controller.VehicleTypeController;
import java.awt.*;
import javax.swing.*;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import model.entities.Clerk;
import model.entities.Space;
import model.entities.User;
import view.ManagementParkingLotView;

public class ParkingLotView extends JInternalFrame implements ActionListener {

    private JPanel panel;
    private JTextField txtName, txtSpaces, txtDisabilitySpaces;
    private JButton btnSave, btnClose, btnExitVehicle, btnView;
    private ParkingLotController controller;
    private ManagementParkingLotView managementView;
    private boolean editMode = false;
    private ParkingLot parkingEditing;
    private final User user;

    public ParkingLotView(ParkingLotController controller,User user) {
        super("Parking Lot Registration", false, true, false, true);
        this.controller = controller;
        this.user = user;
        init();
    }

    private void init() {
        setSize(500, 350);
        setLocation(240, 100);
        setResizable(false);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(200, 220, 240));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel header = new JLabel("Parking Lot Registration", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        outerPanel.add(header, BorderLayout.NORTH);
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Parking Lot Name:"), gbc);
        txtName = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Total Spaces:"), gbc);
        txtSpaces = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtSpaces, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Disability Spaces:"), gbc);
        txtDisabilitySpaces = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtDisabilitySpaces, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        btnSave = new JButton("Register");
        btnSave.setBackground(new Color(60, 179, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this);
        btnClose = new JButton("Close");
        btnClose.setBackground(new Color(220, 20, 60));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> setVisible(false));
        btnExitVehicle = new JButton("Remove Vehicle");
        btnExitVehicle.setBackground(new Color(255, 165, 0));
        btnExitVehicle.setForeground(Color.WHITE);
        btnExitVehicle.setFocusPainted(false);
        btnExitVehicle.addActionListener(this);
        btnView = new JButton("View Parking Lots");
        btnView.setBackground(new Color(70, 130, 180));
        btnView.setForeground(Color.WHITE);
        btnView.setFocusPainted(false);
        btnView.addActionListener(this);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClose);
        buttonPanel.add(btnExitVehicle);
        buttonPanel.add(btnView);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        outerPanel.add(panel, BorderLayout.CENTER);
        add(outerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            registerParkingLot();
        }
        if (e.getSource() == btnExitVehicle) {
            exitVehicleAndCalculateFee();
        }
        if (e.getSource() == btnView) {
            openManagement();
        }
    }

    private void openManagement() {
        if (managementView == null || managementView.isClosed()) {
            managementView = new ManagementParkingLotView(this, controller);
            getDesktopPane().add(managementView);
        }
        managementView.setVisible(true);
        managementView.toFront();
    }

    public void loadParkingForEdit(ParkingLot parkingLot) {
        setTitle("Edit Parking Lot");
        editMode = true;
        parkingEditing = parkingLot;
        txtName.setText(parkingLot.getName());
        txtSpaces.setText(String.valueOf(parkingLot.getSpaces().length));
        long disabilityCount = Arrays.stream(parkingLot.getSpaces()).filter(Space::isDisabilityAdaptation).count();
        txtDisabilitySpaces.setText(String.valueOf(disabilityCount));
        btnSave.setText("Update");
        toFront();
    }

    private void registerParkingLot() {
        if (txtName.getText().isEmpty() || txtSpaces.getText().isEmpty() || txtDisabilitySpaces.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be completed");
            return;
        }
        int totalSpaces, disabilitySpaces;
        try {
            totalSpaces = Integer.parseInt(txtSpaces.getText());
            disabilitySpaces = Integer.parseInt(txtDisabilitySpaces.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Values must be valid numbers");
            return;
        }
        if (totalSpaces <= 0) {
            JOptionPane.showMessageDialog(this, "Total spaces must be greater than 0");
            return;
        }
        if (disabilitySpaces < 0 || disabilitySpaces > totalSpaces) {
            JOptionPane.showMessageDialog(this, "Disability spaces cannot exceed total spaces");
            return;
        }
        if (editMode) {
            String result = controller.updateParkingLot(parkingEditing.getId(), txtName.getText(), totalSpaces, disabilitySpaces);
            JOptionPane.showMessageDialog(this, result);
            SpaceController spaceController = new SpaceController();
            VehicleTypeController vehicleTypeController = new VehicleTypeController();
            SpaceView spaceView = new SpaceView(parkingEditing, spaceController, vehicleTypeController);
            JDesktopPane desktop = getDesktopPane();
            desktop.add(spaceView);
            spaceView.setVisible(true);
            spaceView.toFront();
            if (managementView != null) {
                managementView.refreshTable();
            }
            clearFields();
            editMode = false;
            btnSave.setText("Register");
            setTitle("Parking Lot Registration");
            return;
        }
        ParkingLot parkingLot = controller.registerParkingLot(txtName.getText(), totalSpaces, disabilitySpaces);
        SpaceController spaceController = new SpaceController();
        VehicleTypeController vehicleTypeController = new VehicleTypeController();
        SpaceView spaceView = new SpaceView(parkingLot, spaceController, vehicleTypeController);
        JDesktopPane desktop = getDesktopPane();
        desktop.add(spaceView);
        spaceView.setVisible(true);
        spaceView.toFront();
        JOptionPane.showMessageDialog(this, "Parking lot registered successfully\nName: " + parkingLot.getName());
        clearFields();
        if (managementView != null) {
            managementView.refreshTable();
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtSpaces.setText("");
        txtDisabilitySpaces.setText("");
        txtName.requestFocus();
    }


    private void exitVehicleAndCalculateFee() {

    ParkingLot parkingLot = null;

    if (user instanceof Clerk clerk) {

        parkingLot = controller.findParkingLotById(clerk.getAssignedParkingId());

        if (parkingLot == null) {
            JOptionPane.showMessageDialog(this,
                    "You do not have a valid parking assigned");
            return;
        }

    } else {

        if (controller.getAllParkingLots().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No parking lots registered");
            return;
        }

        StringBuilder info = new StringBuilder("Available parking lots:\n\n");

        for (ParkingLot p : controller.getAllParkingLots()) {
            info.append("ID: ")
                .append(p.getId())
                .append(" - ")
                .append(p.getName())
                .append("\n");
        }

        int parkingLotId;

        try {
            parkingLotId = Integer.parseInt(
                    JOptionPane.showInputDialog(info +
                            "\nEnter the parking lot ID")
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
            return;
        }

        parkingLot = controller.findParkingLotById(parkingLotId);

        if (parkingLot == null) {
            JOptionPane.showMessageDialog(this,
                    "Parking lot not found");
            return;
        }
    }

    if (parkingLot.getVehicles().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "This parking lot has no vehicles");
        return;
    }

    StringBuilder vehiclesInfo =
            new StringBuilder("Vehicles in the parking lot:\n\n");

    for (Vehicle v : parkingLot.getVehicles()) {
        vehiclesInfo.append("- ")
                    .append(v.getPlate())
                    .append("\n");
    }

    String plate = JOptionPane.showInputDialog(
            vehiclesInfo +
            "\nEnter the vehicle plate to remove:"
    );

    if (plate == null || plate.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "You must enter a plate");
        return;
    }

    try {

        double total =
                controller.removeVehicleByPlateAndCalculateFee(plate, parkingLot);

        JOptionPane.showMessageDialog(this,
                "Vehicle removed successfully\n" +
                "Plate: " + plate +
                "\nTotal to pay: ₡" + total);

    } catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(this,
                ex.getMessage());
    }
}
}
