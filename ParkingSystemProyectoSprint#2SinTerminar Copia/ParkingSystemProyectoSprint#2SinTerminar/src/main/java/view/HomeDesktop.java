/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class HomeDesktop extends JDesktopPane {

    private Image background;
    private JLabel header;

    public HomeDesktop() {
        URL location = getClass().getResource("/imagenPark.png");
        if (location != null) {
            background = new ImageIcon(location).getImage();
        }
        header = new JLabel("Sistema de Parqueo", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(255, 255, 255, 60));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
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

