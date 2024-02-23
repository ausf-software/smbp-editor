package io.github.ausf_software.smbp_editor.render;

import javax.swing.*;
import java.awt.*;

public class LoadingWindow extends JFrame {

    private JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(new ImageIcon("res/loading_background.png").getImage(), 0, 0, this);
        }
    };

    public LoadingWindow() {
        setUndecorated(true);
        setResizable(false);
        setSize(650, 450);
        setLocationRelativeTo(null);
        add(panel);
        setVisible(true);
    }

}
