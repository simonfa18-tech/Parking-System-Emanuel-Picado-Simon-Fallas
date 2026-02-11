package view;

import controller.CustomerController;
import controller.ParkingLotController;
import javax.swing.*;
import model.entities.Customer;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.Vehicle;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ManagementVehicleView extends JInternalFrame {

    JTable table;
    DefaultTableModel model;

    JButton buttonEdit;
    JButton buttonDelete;
    JButton buttonAddOwner;

    ParkingLotController parkingController;
    CustomerController customerController;

    String[] headers = {"Parqueo", "Placa", "Tipo", "Dueños"};

    public ManagementVehicleView(ParkingLotController parkingController, CustomerController customerController) {
        super("Vehículos en Parqueo", false, true, false, true);
        this.parkingController = parkingController;
        this.customerController = customerController;
        init();
        loadTable();
    }

    private void init() {
        setSize(700, 380);
        setLocation(220, 110);
        setLayout(null);
        setVisible(true);

        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 650, 250);
        add(scroll);

        buttonEdit = new JButton("Editar");
        buttonEdit.setBounds(130, 290, 120, 25);
        add(buttonEdit);

        buttonDelete = new JButton("Eliminar");
        buttonDelete.setBounds(290, 290, 120, 25);
        add(buttonDelete);

        buttonAddOwner = new JButton("Agregar dueño");
        buttonAddOwner.setBounds(450, 290, 160, 25);
        add(buttonAddOwner);

        buttonDelete.addActionListener(e -> deleteVehicle());
        buttonAddOwner.addActionListener(e -> addOwner());
    }

    private void loadTable() {
        model = new DefaultTableModel(headers, 0);
        table.setModel(model);

        for (ParkingLot parking : parkingController.getAllParkingLots()) {
            for (Vehicle vehicle : parking.getVehicles()) {
                model.addRow(new Object[]{
                    parking.getName(),
                    vehicle.getPlate(),
                    vehicle.getType().getDescription(),
                    ownersAsText(vehicle.getOwners())
                });
            }
        }
    }

    public void refreshTable() {
        model.setRowCount(0);
        loadTable();
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
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo");
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
                        JOptionPane.showMessageDialog(this, "Vehículo eliminado");
                        return;
                    }
                }
            }
        }
    }

    private void addOwner() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo");
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
            JOptionPane.showMessageDialog(this, "Vehículo no encontrado");
            return;
        }

        String id = JOptionPane.showInputDialog(this, "ID del cliente");
        if (id == null || id.isEmpty()) {
            return;
        }

        Customer customer = customerController.findCustomerById(id);
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Cliente no existe");
            return;
        }

        if (vehicleFound.getOwners().stream().anyMatch(c -> c.getId().equals(id))) {
            JOptionPane.showMessageDialog(this, "El cliente ya es dueño de este vehículo");
            return;
        }

        vehicleFound.getOwners().add(customer);

        boolean needsDisabilitySpace = vehicleFound.getOwners().stream()
                .anyMatch(Customer::isDisabilityPresented);

        Space currentSpace = vehicleFound.getSpace();

        if (needsDisabilitySpace && (currentSpace == null || !currentSpace.isDisabilityAdaptation())) {
            int newSpaceId = parkingController.reassignVehicleToDisabilitySpace(vehicleFound, parkingFound);
            if (newSpaceId == -1) {
                JOptionPane.showMessageDialog(this,
                        "No hay espacio adaptado disponible para este vehículo.\n"
                        + "El dueño se agregó, pero el vehículo permanece en su espacio original.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Dueño agregado y vehículo asignado al espacio adaptado: " + newSpaceId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Dueño agregado correctamente");
        }

        refreshTable();
    }
}
