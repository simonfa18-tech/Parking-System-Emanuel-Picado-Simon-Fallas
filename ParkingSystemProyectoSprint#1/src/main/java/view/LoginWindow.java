package view;

import controller.ClerkController;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import model.entities.Clerk;
import model.entities.User;

/**
 *
 * @author Lab01
 */
public class LoginWindow extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignIn;
    ClerkController clerkController = new ClerkController();

    public LoginWindow() {
        setTitle("Login");
        setSize(380, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    public static void main(String[] args) {
        new LoginWindow().setVisible(true);
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblUsername = new JLabel("Usuario:");
        txtUsername = new JTextField(15);
        usernamePanel.add(lblUsername, BorderLayout.WEST);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);

        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);
        passwordPanel.add(lblPassword, BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        btnSignIn = new JButton("Iniciar sesion");
        btnSignIn.addActionListener(this);
        buttonPanel.add(btnSignIn);

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSignIn) {

            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Se requiere usuario y contraseña",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {

                User userAuthenticated = clerkController.searchUser(new Clerk(0, "", 0, null, "", "", username, password));
                if (userAuthenticated == null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El usuario no existe",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Bienvenido " + userAuthenticated.getName(),
                            "Sesion iniciada",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    MainMenuView mainMenuView = new MainMenuView();
                    mainMenuView.open();

                }
            }
        }
    }
} //Terminado
