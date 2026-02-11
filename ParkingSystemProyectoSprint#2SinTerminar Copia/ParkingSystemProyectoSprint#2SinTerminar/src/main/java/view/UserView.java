package view;

import controller.UserController;
import model.entities.User;
import model.entities.Administrator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView extends JInternalFrame implements ActionListener {

    private final UserController controller;
    private JTextField txtUsername, txtName, txtAge, txtPhone, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRole;
    private JButton btnSave, btnViewUsers, btnExit;
    private ManagementUserView managementView;
    private boolean editMode = false;
    private User userEditing = null;

    public UserView(UserController controller) {
        super("User Management", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(480, 420);
        setLocation(240, 100);
        setResizable(false);
        setVisible(true);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(200, 220, 240));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel header = new JLabel("User Management", SwingConstants.CENTER);
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
        formPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(txtUsername, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Name:"), gbc);
        txtName = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Age:"), gbc);
        txtAge = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(txtAge, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(txtPhone, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Role:"), gbc);
        comboRole = new JComboBox<>(new String[]{"Clerk", "Administrator"});
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(comboRole, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        btnSave = new JButton("Create");
        btnSave.setBackground(new Color(60, 179, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this);
        btnViewUsers = new JButton("View Users");
        btnViewUsers.setBackground(new Color(70, 130, 180));
        btnViewUsers.setForeground(Color.WHITE);
        btnViewUsers.setFocusPainted(false);
        btnViewUsers.addActionListener(this);
        btnExit = new JButton("Exit");
        btnExit.setBackground(new Color(220, 20, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> dispose());
        buttonPanel.add(btnSave);
        buttonPanel.add(btnViewUsers);
        buttonPanel.add(btnExit);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        outerPanel.add(formPanel, BorderLayout.CENTER);
        add(outerPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            if (editMode) {
                updateUser();
            } else {
                createUser();
            }
        }
        if (e.getSource() == btnViewUsers) {
            openManagement();
        }
    }

    private void createUser() {
        try {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String name = txtName.getText();
            String ageText = txtAge.getText().trim();
            if (ageText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Age is required");
                return;
            }
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Age must be a number");
                return;
            }
            if (age < 18) {
                JOptionPane.showMessageDialog(this, "Age must be 18 or older");
                return;
            }
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String role = comboRole.getSelectedItem().toString();
            String msg;
            if (role.equals("Clerk")) {
                msg = controller.createClerk(username, password, name, age, phone, email);
            } else {
                msg = controller.createAdministrator(username, password, name, age, phone, email);
            }
            JOptionPane.showMessageDialog(this, msg);
            clear();
            if (managementView != null) {
                managementView.refreshTable();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid age");
        }
    }

    private void clear() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }

    private void openManagement() {
        if (managementView == null || managementView.isClosed()) {
            managementView = new ManagementUserView(controller, this);
            getDesktopPane().add(managementView);
        }
        managementView.setVisible(true);
        managementView.toFront();
    }

    public void loadUserForEdit(User user) {
        editMode = true;
        userEditing = user;
        txtUsername.setText(user.getUsername());
        txtUsername.setEditable(false);
        txtPassword.setText(user.getPassword());
        txtName.setText(user.getName());
        txtAge.setText(String.valueOf(user.getAge()));
        txtPhone.setText(user.getPhone());
        txtEmail.setText(user.getEmail());
        comboRole.setSelectedItem(user instanceof Administrator ? "Administrator" : "Clerk");
        btnSave.setText("Update");
    }

    private void updateUser() {
        String name = txtName.getText();
        String ageText = txtAge.getText().trim();
        if (ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Age is required");
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number");
            return;
        }
        if (age < 18) {
            JOptionPane.showMessageDialog(this, "Age must be 18 or older");
            return;
        }
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        userEditing.setName(name);
        userEditing.setAge(age);
        userEditing.setPhone(phone);
        userEditing.setEmail(email);
        String msg = controller.updateUser(userEditing);
        JOptionPane.showMessageDialog(this, msg);
        editMode = false;
        userEditing = null;
        btnSave.setText("Create");
        txtUsername.setEditable(true);
        clear();
        if (managementView != null) {
            managementView.refreshTable();
        }
    }
}

