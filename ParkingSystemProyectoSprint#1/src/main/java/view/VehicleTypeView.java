/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.VehicleTypeController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.entities.VehicleType;

/**
 *
 * @author PC
 */
public class VehicleTypeView extends JInternalFrame implements ActionListener {

    private final VehicleTypeController controller;
    JTextField txtId, txtDescription, txtTires, txtFee;
    JButton btnSave;

    public VehicleTypeView(VehicleTypeController controller) {
        super("Registrar Tipo de Vehículo", false, true, false, true);
        this.controller = controller;
        init();
    }

    private void init() {
        setSize(400, 300);
        setLocation(200, 100);
        setVisible(true);
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        add(panel);
        panel.add(label("ID:", 40, 30));
        txtId = field(160, 30);
        panel.add(label("Descripción:", 40, 70));
        txtDescription = field(160, 70);
        panel.add(label("Número de llantas:", 40, 110));
        txtTires = field(160, 110);
        panel.add(label("Tarifa:", 40, 150));
        txtFee = field(160, 150);
        btnSave = new JButton("Guardar");
        btnSave.setBounds(160, 200, 100, 25);
        btnSave.addActionListener(this);
        panel.add(btnSave);
    }

    private JLabel label(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 140, 25);
        return l;
    }

    private JTextField field(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 180, 25);
        return f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            try {
                int id = Integer.parseInt(txtId.getText());
                String description = txtDescription.getText();
                int tires = Integer.parseInt(txtTires.getText());
                double fee = Double.parseDouble(txtFee.getText());
                VehicleType type = new VehicleType(id, description, tires, fee);
                String result = controller.insertVehicleType(type);
                JOptionPane.showMessageDialog(this, result);
                clear();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en los datos ingresados");
            }
        }
    }

    private void clear() {
        txtId.setText("");
        txtDescription.setText("");
        txtTires.setText("");
        txtFee.setText("");
    }
}
