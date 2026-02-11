/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.UserController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.*;
import javax.swing.*;
import model.entities.Administrator;
import model.entities.User;

public class MainMenuView extends JFrame {

    private final User user;
    private final CustomerController customerController;
    private final VehicleController vehicleController;
    private final ParkingLotController parkingLotController;
    private final VehicleTypeController vehicleTypeController;
    private final UserController userController;
    private JDesktopPane desktop;
    private JMenuBar menuBar;

    public MainMenuView(User user, CustomerController customerController, VehicleController vehicleController, ParkingLotController parkingLotController, VehicleTypeController vehicleTypeController, UserController userController) {
        this.user = user;
        this.customerController = customerController;
        this.vehicleController = vehicleController;
        this.parkingLotController = parkingLotController;
        this.vehicleTypeController = vehicleTypeController;
        this.userController = userController;
        init();
    }

    private void init() {
        setTitle("Parking System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel lblTitle = new JLabel("Parking Management System", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblUser = new JLabel("Logged in as: " + user.getName(), SwingConstants.RIGHT);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 20, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(lblUser);
        rightPanel.add(btnLogout);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        desktop = new JDesktopPane() {
            private final Image background = new ImageIcon(getClass().getResource("/imagenPark.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (background != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(255, 255, 255, 60));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            }
        };
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(desktop, BorderLayout.CENTER);
        setContentPane(contentPanel);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        createModules();
    }

    private void createModules() {
        JMenu menuVehicles = new JMenu("Vehicles");
        JMenuItem itemVehicle = new JMenuItem("Manage Vehicles");
        itemVehicle.addActionListener(e -> openInternal(new VehicleView(vehicleController, customerController, parkingLotController, vehicleTypeController)));
        JMenuItem itemVehicleType = new JMenuItem("Vehicle Types");
        itemVehicleType.addActionListener(e -> openInternal(new VehicleTypeView(vehicleTypeController)));
        menuVehicles.add(itemVehicle);
        menuVehicles.add(itemVehicleType);
        menuBar.add(menuVehicles);
        JMenu menuCustomers = new JMenu("Customers");
        JMenuItem itemCustomer = new JMenuItem("Manage Customers");
        itemCustomer.addActionListener(e -> openInternal(new CustomerView(customerController)));
        menuCustomers.add(itemCustomer);
        menuBar.add(menuCustomers);
        JMenu menuParking = new JMenu("Parking Lots");
        JMenuItem itemParking = new JMenuItem("Manage Parking Lots");
        itemParking.addActionListener(e -> openInternal(new ParkingLotView(parkingLotController)));
        menuParking.add(itemParking);
        menuBar.add(menuParking);
        if (user instanceof Administrator) {
            JMenu menuUsers = new JMenu("Users");
            JMenuItem itemUsers = new JMenuItem("Manage Users");
            itemUsers.addActionListener(e -> openInternal(new UserView(userController)));
            menuUsers.add(itemUsers);
            menuBar.add(menuUsers);
        }
    }

    private void openInternal(JInternalFrame frame) {
        desktop.add(frame);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (Exception ignored) {
        }
    }
}

