package io.github.ausf_software.smbp_editor.render;

import io.github.ausf_software.smbp_editor.core.EditorToolsManager;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("SMBP Editor");
        setSize(700, 700);
        add(ToolsPanel.INSTANCE, BorderLayout.WEST);
        add(new ModePanel(), BorderLayout.NORTH);

        Editor.INSTANCE.registerInputMap(EditorToolsManager.INSTANCE.getInputMap());
        Editor.INSTANCE.registerActionMap(EditorToolsManager.INSTANCE.getActionMap());
        Editor.INSTANCE.registerMouseWheelMap(EditorToolsManager.INSTANCE.getMouseWheelMap());

        add(Editor.INSTANCE, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
