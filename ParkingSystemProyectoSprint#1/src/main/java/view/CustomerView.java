/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.Customer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerView extends JInternalFrame implements ActionListener {

    private JPanel panel;
    private JLabel labelId, labelName;
    private JTextField textFieldId, textFieldName;
    private JCheckBox checkDisability;
    private JButton buttonSave, buttonCancel, buttonView;

    private boolean editMode = false;

    private final CustomerController controller;
    private ManagementCustomerView managementCustomerView;

    public CustomerView(CustomerController controller) {
        super("Registro de Clientes", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(460, 300);
        setLocation(220, 80);
        setResizable(false);
        setVisible(true);

        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);

        labelId = new JLabel("Identificación:");
        labelId.setBounds(50, 30, 100, 25);
        panel.add(labelId);

        textFieldId = new JTextField();
        textFieldId.setBounds(160, 30, 220, 25);
        panel.add(textFieldId);

        labelName = new JLabel("Nombre:");
        labelName.setBounds(50, 70, 100, 25);
        panel.add(labelName);

        textFieldName = new JTextField();
        textFieldName.setBounds(160, 70, 220, 25);
        panel.add(textFieldName);

        checkDisability = new JCheckBox("Presenta discapacidad");
        checkDisability.setBounds(160, 110, 220, 25);
        checkDisability.setBackground(Color.WHITE);
        panel.add(checkDisability);

        buttonSave = new JButton("Guardar");
        buttonSave.setBounds(60, 180, 100, 25);
        buttonSave.addActionListener(this);
        panel.add(buttonSave);

        buttonView = new JButton("Ver clientes");
        buttonView.setBounds(180, 180, 120, 25);
        buttonView.addActionListener(this);
        panel.add(buttonView);

        buttonCancel = new JButton("Cerrar");
        buttonCancel.setBounds(320, 180, 80, 25);
        buttonCancel.addActionListener(e -> setVisible(false));
        panel.add(buttonCancel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSave) {
            saveCustomer();
        } else if (e.getSource() == buttonView) {
            openCustomerManagement();
        }
    }

    private void saveCustomer() {
        if (textFieldId.getText().isEmpty() || textFieldName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos");
            return;
        }

        Customer customer = new Customer(
                textFieldId.getText(),
                textFieldName.getText(),
                checkDisability.isSelected()
        );

        String result = editMode
                ? controller.updateCustomer(customer)
                : controller.insertCustomer(customer);

        JOptionPane.showMessageDialog(this, result);

        if (managementCustomerView != null) {
            managementCustomerView.refreshTable();
        }

        clearFields();
    }

    private void openCustomerManagement() {
        if (managementCustomerView == null || managementCustomerView.isClosed()) {
            managementCustomerView = new ManagementCustomerView(this, controller);
            JDesktopPane desktop = getDesktopPane();
            desktop.add(managementCustomerView);
        }

        managementCustomerView.setVisible(true);
        managementCustomerView.toFront();
    }

    public void loadCustomerForEdit(Customer customer) {
        setTitle("Editar Cliente");
        editMode = true;

        textFieldId.setText(customer.getId());
        textFieldId.setEditable(false);
        textFieldName.setText(customer.getName());
        checkDisability.setSelected(customer.isDisabilityPresented());

        buttonSave.setText("Modificar");
        toFront();
    }

    private void clearFields() {
        if (!editMode) {
            textFieldId.setText("");
        }
        textFieldName.setText("");
        checkDisability.setSelected(false);
        textFieldId.requestFocus();
    }
}
