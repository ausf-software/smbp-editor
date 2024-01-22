package io.github.ausf_software.smbp_editor;

import io.github.ausf_software.smbp_editor.tools.EditorToolsManager;
import io.github.ausf_software.smbp_editor.window.MainWindow;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        EditorToolsManager.INSTANCE.load();
        new MainWindow();
    }

}
