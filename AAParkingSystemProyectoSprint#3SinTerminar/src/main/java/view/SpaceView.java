/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.SpaceController;
import controller.VehicleTypeController;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import model.entities.ParkingLot;
import model.entities.Space;
import model.entities.VehicleType;

/**
 *
 * @author emman
 */
public class SpaceView extends JInternalFrame {

    private ParkingLot parkingLot;
    private SpaceController controller;
    private VehicleTypeController vehicleTypeController;

    public SpaceView(ParkingLot parkingLot, SpaceController controller, VehicleTypeController vehicleTypeController) {
        super("Space configuration", false, true, false, true);
        this.parkingLot = parkingLot;
        this.controller = controller;
        this.vehicleTypeController = vehicleTypeController;

        init();
    }

    private void init() {
        for (Space space : parkingLot.getSpaces()) {

            VehicleType selectedType = (VehicleType) JOptionPane.showInputDialog(
                    this,
                    "Select type of space #" + space.getId() + "Disability: " + (space.isDisabilityAdaptation() ? "si" : "no"),
                    "Set up space",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    vehicleTypeController.getAllVehicleTypes().toArray(),
                    null
            );

            if (selectedType != null) {
                controller.configureSpace(space, selectedType);
            }
        }

        JOptionPane.showMessageDialog(this, "Spaces configured correctly");
        dispose();
    }

}
