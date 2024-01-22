package io.github.ausf_software.smbp_editor.window;

import io.github.ausf_software.smbp_editor.tools.EditorToolsManager;
import io.github.ausf_software.smbp_editor.window.panels.Editor;
import io.github.ausf_software.smbp_editor.window.panels.ModePanel;
import io.github.ausf_software.smbp_editor.window.panels.ToolsPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("SMBP Editor");
        setSize(700, 700);
        add(new ToolsPanel(), BorderLayout.WEST);
        add(new ModePanel(), BorderLayout.NORTH);

        Editor.INSTANCE.registerInputMap(EditorToolsManager.INSTANCE.getInputMap());
        Editor.INSTANCE.registerActionMap(EditorToolsManager.INSTANCE.getActionMap());

        add(Editor.INSTANCE, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
