/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.ReportService;
import controller.UserController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.ParkingLot;
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

    public MainMenuView(User user,
            CustomerController customerController,
            VehicleController vehicleController,
            ParkingLotController parkingLotController,
            VehicleTypeController vehicleTypeController,
            UserController userController) {

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

        String userInfo = "Logged in as: " + user.getName();

        if (user instanceof Clerk clerk) {
            ParkingLot assigned
                    = parkingLotController.findParkingLotById(clerk.getAssignedParkingId());
            if (assigned != null) {
                userInfo += " | Parking: " + assigned.getName();
            }
        }

        JLabel lblUser = new JLabel(userInfo, SwingConstants.RIGHT);
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
            private final Image background
                    = new ImageIcon(getClass().getResource("/imagenPark.png")).getImage();

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
        itemVehicle.addActionListener(e
                -> openInternal(new VehicleView(vehicleController,
                        customerController,
                        parkingLotController,
                        vehicleTypeController,
                        user)));

        JMenuItem itemVehicleType = new JMenuItem("Vehicle Types");
        itemVehicleType.addActionListener(e
                -> openInternal(new VehicleTypeView(vehicleTypeController)));

        menuVehicles.add(itemVehicle);
        menuVehicles.add(itemVehicleType);
        menuBar.add(menuVehicles);

        JMenu menuCustomers = new JMenu("Customers");
        JMenuItem itemCustomer = new JMenuItem("Manage Customers");
        itemCustomer.addActionListener(e
                -> openInternal(new CustomerView(customerController)));
        menuCustomers.add(itemCustomer);
        menuBar.add(menuCustomers);

        JMenu menuParking = new JMenu("Parking Lots");
        JMenuItem itemParking = new JMenuItem("Manage Parking Lots");
        itemParking.addActionListener(e
                -> openInternal(new ParkingLotView(parkingLotController, user)));
        menuParking.add(itemParking);
        menuBar.add(menuParking);

        if (user instanceof Administrator) {
            JMenu menuUsers = new JMenu("Users");
            JMenuItem itemUsers = new JMenuItem("Manage Users");
            itemUsers.addActionListener(e
                    -> openInternal(new UserView(userController)));
            menuUsers.add(itemUsers);
            menuBar.add(menuUsers);
        }

        JMenu menuReports = new JMenu("Reports");
        JMenuItem itemDaily = new JMenuItem("Generate Daily Report");

        itemDaily.addActionListener(e -> {

            ParkingLot selected = null;

            if (user instanceof Clerk clerk) {
                selected = parkingLotController
                        .findParkingLotById(clerk.getAssignedParkingId());
                if (selected == null) {
                    JOptionPane.showMessageDialog(this,
                            "Clerk has no valid parking assigned");
                    return;
                }
            } else {
                selected = showParkingSelector();
            }

            if (selected != null) {
                try {
                    String fileName
                            = ReportService.generateDailyReport(selected, user);
                    if (fileName == null) {
                        JOptionPane.showMessageDialog(this,
                                "No vehicles to report.");
                        return;
                    }
                    parkingLotController.persist();
                    JOptionPane.showMessageDialog(this,
                            "Report generated: " + fileName);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error generating report.");
                }
            }
        });

        menuReports.add(itemDaily);
        menuBar.add(menuReports);
    }

    private ParkingLot showParkingSelector() {

        JDialog dialog = new JDialog(this, "Select Parking", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextField txtSearch = new JTextField();
        DefaultListModel<ParkingLot> model = new DefaultListModel<>();

        parkingLotController.getAllParkingLots()
                .stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .forEach(model::addElement);

        JList<ParkingLot> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(list);

        JButton btnSelect = new JButton("Select");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        topPanel.add(txtSearch, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnSelect);

        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void filter() {
                String text = txtSearch.getText().toLowerCase();
                model.clear();

                parkingLotController.getAllParkingLots()
                        .stream()
                        .sorted((a, b)
                                -> a.getName().compareToIgnoreCase(b.getName()))
                        .filter(p
                                -> p.getName().toLowerCase().contains(text))
                        .forEach(model::addElement);
            }

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        final ParkingLot[] selected = new ParkingLot[1];

        btnSelect.addActionListener(e -> {
            selected[0] = list.getSelectedValue();
            dialog.dispose();
        });

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    selected[0] = list.getSelectedValue();
                    dialog.dispose();
                }
            }
        });

        dialog.setVisible(true);

        return selected[0];
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
