/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Clerk;

/**
 *
 * @author Lab01
 */
public class ClerkData {
    
    ArrayList<Clerk> clerks = new ArrayList<>();

    public ArrayList<Clerk> getAllClerks() {
         clerks.add(new Clerk(0, "a", 0, null, "a", "admin", "admin", "123"));

        return clerks;
    }
}
