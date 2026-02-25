package view;

import controller.CustomerController;
import controller.ParkingLotController;
import controller.UserController;
import controller.VehicleController;
import controller.VehicleTypeController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.entities.User;

public class LoginWindow extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignIn, btnClear;

    private final CustomerController customerController = new CustomerController();
    private final ParkingLotController parkingLotController = new ParkingLotController();
    private final VehicleController vehicleController = new VehicleController(parkingLotController);
    private final VehicleTypeController vehicleTypeController = new VehicleTypeController();
    private final UserController userController = new UserController();

    public LoginWindow() {
        setTitle("System Access");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
    }

    private void initComponents() {

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(200, 220, 240));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Welcome to Parking System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        outerPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        // BOTÓN SIGN IN
        btnSignIn = new JButton("Sign In");
        btnSignIn.setBackground(new Color(60, 179, 113));
        btnSignIn.setForeground(Color.WHITE);
        btnSignIn.setFocusPainted(false);
        btnSignIn.addActionListener(this);

        // BOTÓN CLEAR
        btnClear = new JButton("Clear");
        btnClear.setBackground(new Color(255, 165, 0)); // Naranja
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(e -> confirmAndClear());

        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        outerPanel.add(formPanel, BorderLayout.CENTER);
        add(outerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSignIn) {
            login();
        }
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password are required",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User userAuthenticated = userController.login(username, password);

        if (userAuthenticated == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Welcome " + userAuthenticated.getName(),
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);

        MainMenuView mainMenuView = new MainMenuView(
                userAuthenticated,
                customerController,
                vehicleController,
                parkingLotController,
                vehicleTypeController,
                userController
        );

        mainMenuView.setVisible(true);
        dispose();
    }


    private void confirmAndClear() {

        if (txtUsername.getText().isEmpty() && txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this,
                    "There is no data to clear.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear the data?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            clearFields();
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
    }
} //Terminado
