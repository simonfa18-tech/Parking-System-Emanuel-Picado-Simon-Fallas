package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.Color;
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
    JPanel panel;
    JTextField txtPlate, txtBrand, txtModel, txtColor;
    JComboBox<VehicleType> comboVehicleType;
    JComboBox<ParkingLot> comboParkingLot;
    JButton btnAddOwner, btnSave, btnViewVehicles;
    ArrayList<Customer> owners = new ArrayList<>();
    ManagementVehicleView managementView;

    public VehicleView(VehicleController vehicleController, CustomerController customerController, ParkingLotController parkingLotController, VehicleTypeController vehicleTypeController) {
        super("Registro de Vehículo", false, true, false, true);
        this.vehicleController = vehicleController;
        this.customerController = customerController;
        this.parkingLotController = parkingLotController;
        this.vehicleTypeController = vehicleTypeController;
        init();
    }

    private void init() {
        setSize(520, 420);
        setLocation(220, 80);
        setVisible(true);
        setResizable(false);
        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);
        panel.add(label("Placa:", 40, 30));
        txtPlate = field(160, 30);
        panel.add(label("Marca:", 40, 70));
        txtBrand = field(160, 70);
        panel.add(label("Modelo:", 40, 110));
        txtModel = field(160, 110);
        panel.add(label("Color:", 40, 150));
        txtColor = field(160, 150);
        panel.add(label("Tipo:", 40, 190));
        comboVehicleType = new JComboBox<>();
        comboVehicleType.setBounds(160, 190, 220, 25);
        loadVehicleTypes();
        panel.add(comboVehicleType);
        panel.add(label("Parqueo:", 40, 230));
        comboParkingLot = new JComboBox<>();
        comboParkingLot.setBounds(160, 230, 220, 25);
        loadParkingLots();
        panel.add(comboParkingLot);
        btnAddOwner = new JButton("Agregar dueño");
        btnAddOwner.setBounds(40, 290, 140, 25);
        btnAddOwner.addActionListener(this);
        panel.add(btnAddOwner);
        btnSave = new JButton("Guardar y parquear");
        btnSave.setBounds(200, 290, 180, 25);
        btnSave.addActionListener(this);
        panel.add(btnSave);
        btnViewVehicles = new JButton("Ver vehículos");
        btnViewVehicles.setBounds(160, 330, 180, 25);
        btnViewVehicles.addActionListener(this);
        panel.add(btnViewVehicles);
    }

    private JLabel label(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 140, 25);
        return l;
    }

    private JTextField field(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 220, 25);
        panel.add(f);
        return f;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddOwner) {
            addOwner();
        }
        if (e.getSource() == btnSave) {
            saveVehicle();
        }
        if (e.getSource() == btnViewVehicles) {
            openManagement();
        }
    }

    private void addOwner() {
        String id = JOptionPane.showInputDialog(this, "ID del cliente");
        if (id == null || id.isEmpty()) {
            return;
        }
        Customer c = customerController.findCustomerById(id);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado");
            return;
        }
        owners.add(c);
        JOptionPane.showMessageDialog(this, "Dueño agregado correctamente");
    }

    private void saveVehicle() {
        if (owners.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un dueño");
            return;
        }

        if (comboVehicleType.getItemCount() == 0 || comboVehicleType.getSelectedItem() == null) {
            VehicleTypeView typeView = new VehicleTypeView(vehicleTypeController);
            typeView.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                @Override
                public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                    loadVehicleTypes();
                }
            });
            getDesktopPane().add(typeView);
            typeView.toFront();
            JOptionPane.showMessageDialog(this, "Primero registre un tipo de vehículo");
            return;
        }

        if (comboParkingLot.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un parqueo");
            return;
        }

        Vehicle vehicle = new Vehicle(
                txtPlate.getText(),
                txtColor.getText(),
                txtBrand.getText(),
                txtModel.getText(),
                new ArrayList<>(owners),
                (VehicleType) comboVehicleType.getSelectedItem()
        );

        vehicleController.insertVehicle(vehicle);

        ParkingLot parking = (ParkingLot) comboParkingLot.getSelectedItem();
        int spaceId = parkingLotController.registerVehicleInParkingLot(vehicle, parking);

        if (spaceId <= 0) {
            JOptionPane.showMessageDialog(this, "No hay espacios disponibles compatibles\npara este tipo de vehículo");
            return;
        }

        JOptionPane.showMessageDialog(this, "Vehículo registrado correctamente\nEspacio asignado: " + spaceId);
        clear();
    }

    private void clear() {
        txtPlate.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtColor.setText("");
        owners.clear();
    }

    private void openManagement() {
        managementView = new ManagementVehicleView(parkingLotController, customerController);
        getDesktopPane().add(managementView);
        managementView.toFront();
    }
}
