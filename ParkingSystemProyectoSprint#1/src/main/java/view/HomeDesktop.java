/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author emman
 */
public class HomeDesktop extends JDesktopPane {

    private Image background;

    public HomeDesktop() {
        URL location = getClass().getResource("/imagenPark.png");

        if (location == null) {
            System.out.println("NO se encontró la imagen");
            return;
        }

        background = new ImageIcon(location).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(
                    background,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );
        }
    }

    public void addInternal(JInternalFrame frame) {
        removeAll();
        repaint();

        add(frame);
        frame.setVisible(true);
        frame.toFront();
    }

}
