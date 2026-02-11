/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author emman
 */
public class MainMenuView extends JFrame {

    private HomeDesktop desktop;
    private JComboBox<String> mainMenu;
    private final CustomerController customerController = new CustomerController();
    private final VehicleController vehicleController = new VehicleController();
    private final ParkingLotController parkingLotController = new ParkingLotController();
    private final VehicleTypeController vehicleTypeController = new VehicleTypeController();

    public MainMenuView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Parqueo");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        desktop = new HomeDesktop();
        add(desktop, BorderLayout.CENTER);

        add(createTopMenu(), BorderLayout.NORTH);
    }

    private JPanel createTopMenu() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        mainMenu = new JComboBox<>(new String[]{
            "Seleccione",
            "Clientes",
            "Vehículos",
            "Parqueos"
        });

        mainMenu.addActionListener(e -> openSelectedModule());

        panel.add(new JLabel("Módulo: "));
        panel.add(mainMenu);
        return panel;
    }

    private void openSelectedModule() {
        String option = (String) mainMenu.getSelectedItem();

        switch (option) {
            case "Clientes" ->
                desktop.addInternal(new CustomerView(customerController));
            case "Vehículos" ->
                desktop.addInternal(new VehicleView(
                        vehicleController,
                        customerController,
                        parkingLotController,
                        vehicleTypeController));
            case "Parqueos" ->
                desktop.addInternal(new ParkingLotView(parkingLotController));
        }
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> new MainMenuView().setVisible(true));
    }

}
