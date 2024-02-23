package io.github.ausf_software.smbp_editor.render;

import io.github.ausf_software.smbp_editor.core.system.SystemManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ToolsPanel extends JPanel {

    public final SystemManager SYSTEM;
    private Map<String, JButton> toolButtons;

    public ToolsPanel(SystemManager systemManager) {
        SYSTEM = systemManager;
        toolButtons = new HashMap<>();
        setBackground(new Color(30, 30, 30));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addTool(String icon, String name) throws IOException {
        JButton button = new JButton();
        button.addActionListener(e -> SYSTEM.changeTool(name));
        Image img = ImageIO.read(new File(icon));
        button.setIcon(new ImageIcon(img));
        button.setBackground(Color.DARK_GRAY);
        toolButtons.put(name, button);
        add(button);
    }

    public void removeTool(String name) {
        JButton button = toolButtons.get(name);
        remove(button);
        repaint();
        toolButtons.remove(name);
    }

}
