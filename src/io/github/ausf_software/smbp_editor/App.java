package io.github.ausf_software.smbp_editor;

import io.github.ausf_software.smbp_editor.core.system.SystemManager;
import io.github.ausf_software.smbp_editor.render.MainWindow;
import org.apache.log4j.BasicConfigurator;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        BasicConfigurator.configure();
        SystemManager systemManager = new SystemManager();
        new MainWindow(systemManager);
    }

}
