/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomerController;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.entities.Customer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ManagementCustomerView extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton buttonEdit, buttonDelete;

    private CustomerController controller;
    private CustomerView customerView;

    private final String[] headers = {"ID", "Nombre", "Discapacidad"};

    public ManagementCustomerView(CustomerView window, CustomerController controller) {
        super("Clientes Registrados", false, true, false, true);
        this.customerView = window;
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(520, 350);
        setLocation(250, 120);
        setLayout(null);
        setVisible(true);

        table = new JTable();
        model = new DefaultTableModel(headers, 0);
        table.setModel(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 470, 200);
        add(scroll);

        buttonEdit = new JButton("Editar");
        buttonEdit.setBounds(120, 240, 100, 25);
        add(buttonEdit);

        buttonDelete = new JButton("Borrar");
        buttonDelete.setBounds(260, 240, 100, 25);
        add(buttonDelete);

        buttonEdit.addActionListener(e -> editCustomer());
        buttonDelete.addActionListener(e -> deleteCustomer());

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        ArrayList<Customer> customers = controller.getAllCustomers();
        for (Customer c : customers) {
            model.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.isDisabilityPresented() ? "Sí" : "No"
            });
        }
    }

    private void editCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        Customer customer = controller.findCustomerById(id);
        customerView.loadCustomerForEdit(customer);
        dispose();
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }
        String id = table.getValueAt(row, 0).toString();
        JOptionPane.showMessageDialog(this, controller.removeCustomer(id));
        refreshTable();
    }
}
