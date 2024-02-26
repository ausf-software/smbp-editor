package io.github.ausf_software.smbp_editor.render;

import io.github.ausf_software.smbp_editor.core.system.SystemManager;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final SystemManager systemManager;
    public MainWindow() {
        systemManager = new SystemManager(this);

        setTitle("SMBP Editor");
        setSize(700, 700);
        add(systemManager.getToolsPanel(), BorderLayout.WEST);
        add(new ModePanel(), BorderLayout.NORTH);

        systemManager.registerInputMapsToEditor(Editor.INSTANCE);

        add(Editor.INSTANCE, BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
