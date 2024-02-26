package io.github.ausf_software.smbp_editor.render;

import javax.swing.*;
import java.awt.*;

public class LoadingWindow extends JFrame {
    private String pathBack = "res/loading_background.png";

    private JPanel panel = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(new ImageIcon(pathBack).getImage(), 0, 0, this);
        }
    };

    private JProgressBar bar = new JProgressBar();

    public LoadingWindow() {
        setUndecorated(true);
        setResizable(false);
        setSize(650, 450);
        setLocationRelativeTo(null);
        add(panel);
        panel.add(bar, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void setCountLoadingItem(int a) {
        bar.setMaximum(a);
    }

    public void addProgress() {
        bar.setValue(bar.getValue() + 1);
    }

}
