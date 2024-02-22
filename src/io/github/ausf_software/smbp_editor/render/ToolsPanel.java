package io.github.ausf_software.smbp_editor.render;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ToolsPanel extends JPanel {

    public static ToolsPanel INSTANCE = new ToolsPanel();
    private Map<String, JButton> toolButtons;

    public ToolsPanel() {
        toolButtons = new HashMap<>();
        setBackground(new Color(30, 30, 30));
    }

    public void addTool(String icon, String name) throws IOException {
        JButton button = new JButton();
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
