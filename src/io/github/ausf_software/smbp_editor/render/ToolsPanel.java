package io.github.ausf_software.smbp_editor.render;

import io.github.ausf_software.smbp_editor.core.system.SystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ToolsPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ToolsPanel.class);

    public final SystemManager SYSTEM;
    private Map<String, JButton> toolButtons;

    public ToolsPanel(SystemManager systemManager) {
        SYSTEM = systemManager;
        toolButtons = new HashMap<>();
        setBackground(new Color(30, 30, 30));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addTool(String icon, String name) {
        JButton button = new JButton();
        button.addActionListener(e -> SYSTEM.changeTool(name));
        Image img = null;
        try {
            img = ImageIO.read(new File(icon));
        } catch (IOException e) {
            log.error("Для инструмента {} не была найдена иконка по адресу {}. Добавление на " +
                            "панель инструментов будет проигнорировано.", name, icon);
        }
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
