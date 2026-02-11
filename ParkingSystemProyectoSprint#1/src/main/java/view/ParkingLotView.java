package view;

import controller.ParkingLotController;
import controller.SpaceController;
import controller.VehicleTypeController;
import java.awt.Color;
import javax.swing.*;
import model.entities.ParkingLot;
import model.entities.Vehicle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParkingLotView extends JInternalFrame implements ActionListener {

    JPanel panel;
    JLabel labelName;
    JLabel labelSpaces;
    JLabel labelDisabilitySpaces;
    JTextField textFieldName;
    JTextField textFieldSpaces;
    JTextField textFieldDisabilitySpaces;
    JButton buttonSave;
    JButton buttonClose;
    JButton buttonExitVehicle;
    ParkingLotController controller;

    public ParkingLotView(ParkingLotController controller) {
        super("Registro de Parqueos", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(460, 300);
        setLocation(240, 100);
        setResizable(false);
        setVisible(true);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        labelName = new JLabel("Nombre del parqueo:");
        labelName.setBounds(40, 30, 150, 25);
        panel.add(labelName);

        textFieldName = new JTextField();
        textFieldName.setBounds(190, 30, 220, 25);
        panel.add(textFieldName);

        labelSpaces = new JLabel("Cantidad total de espacios:");
        labelSpaces.setBounds(40, 70, 180, 25);
        panel.add(labelSpaces);

        textFieldSpaces = new JTextField();
        textFieldSpaces.setBounds(190, 70, 220, 25);
        panel.add(textFieldSpaces);

        labelDisabilitySpaces = new JLabel("Espacios con discapacidad:");
        labelDisabilitySpaces.setBounds(40, 110, 180, 25);
        panel.add(labelDisabilitySpaces);

        textFieldDisabilitySpaces = new JTextField();
        textFieldDisabilitySpaces.setBounds(190, 110, 220, 25);
        panel.add(textFieldDisabilitySpaces);

        buttonSave = new JButton("Registrar");
        buttonSave.setBounds(80, 170, 120, 25);
        buttonSave.addActionListener(this);
        panel.add(buttonSave);

        buttonClose = new JButton("Cerrar");
        buttonClose.setBounds(230, 170, 120, 25);
        buttonClose.addActionListener(e -> setVisible(false));
        panel.add(buttonClose);

        buttonExitVehicle = new JButton("Sacar vehículo");
        buttonExitVehicle.setBounds(150, 210, 160, 25);
        buttonExitVehicle.addActionListener(this);
        panel.add(buttonExitVehicle);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSave) {
            registerParkingLot();
        }
        if (e.getSource() == buttonExitVehicle) {
            exitVehicleAndCalculateFee();
        }
    }

    private void registerParkingLot() {
        if (textFieldName.getText().isEmpty()
                || textFieldSpaces.getText().isEmpty()
                || textFieldDisabilitySpaces.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos");
            return;
        }

        int totalSpaces;
        int disabilitySpaces;

        try {
            totalSpaces = Integer.parseInt(textFieldSpaces.getText());
            disabilitySpaces = Integer.parseInt(textFieldDisabilitySpaces.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Las cantidades deben ser números válidos");
            return;
        }

        if (totalSpaces <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad total de espacios debe ser mayor a 0");
            return;
        }

        if (disabilitySpaces < 0 || disabilitySpaces > totalSpaces) {
            JOptionPane.showMessageDialog(this, "Los espacios con discapacidad no pueden ser mayores al total");
            return;
        }

        ParkingLot parkingLot = controller.registerParkingLot(
                textFieldName.getText(),
                totalSpaces,
                disabilitySpaces
        );
        SpaceController spaceController = new SpaceController();
        VehicleTypeController vehicleTypeController = new VehicleTypeController();
        SpaceView spaceView = new SpaceView(parkingLot, spaceController, vehicleTypeController);
        getParent().add(spaceView);
        spaceView.setVisible(true);

        JOptionPane.showMessageDialog(this,
                "Parqueo registrado correctamente\n"
                + "Nombre: " + parkingLot.getName()
                + "\nEspacios totales: " + totalSpaces
                + "\nEspacios con discapacidad: " + disabilitySpaces);

        clearFields();
    }

    private void clearFields() {
        textFieldName.setText("");
        textFieldSpaces.setText("");
        textFieldDisabilitySpaces.setText("");
        textFieldName.requestFocus();
    }

    private void exitVehicleAndCalculateFee() {
        if (controller.getAllParkingLots().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay parqueos registrados");
            return;
        }

        String parkingLotsInfo = "Parqueos disponibles:\n\n";
        for (ParkingLot parkingLot : controller.getAllParkingLots()) {
            parkingLotsInfo += "ID: " + parkingLot.getId() + " - " + parkingLot.getName() + "\n";
        }

        int parkingLotId;
        try {
            parkingLotId = Integer.parseInt(
                    JOptionPane.showInputDialog(parkingLotsInfo + "\nIngrese el ID del parqueo")
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido");
            return;
        }

        ParkingLot parkingLot = controller.findParkingLotById(parkingLotId);

        if (parkingLot == null || parkingLot.getVehicles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este parqueo no tiene vehículos");
            return;
        }

        String vehiclesInfo = "Vehículos en el parqueo:\n\n";
        for (Vehicle v : parkingLot.getVehicles()) {
            vehiclesInfo += "- " + v.getPlate() + "\n";
        }

        String plate = JOptionPane.showInputDialog(
                vehiclesInfo + "\nIngrese la placa del vehículo a retirar:"
        );

        if (plate == null || plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una placa");
            return;
        }

        try {
            double total = controller.removeVehicleByPlateAndCalculateFee(plate, parkingLot);
            JOptionPane.showMessageDialog(this,
                    "Vehículo retirado correctamente\n"
                    + "Placa: " + plate
                    + "\nTotal a pagar: ₡" + total);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
