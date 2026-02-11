/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.data.SpaceData;
import model.entities.Space;
import model.entities.VehicleType;

/**
 *
 * @author emman
 */
public class SpaceController {

    private final SpaceData data;

    public SpaceController() {
        data = new SpaceData();
    }

    public void configureSpace(Space space, VehicleType type) {
        data.assignVehicleTypeToSpace(space, type);
    }
}
