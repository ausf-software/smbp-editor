package io.github.ausf_software.smbp_editor;

import io.github.ausf_software.smbp_editor.render.MainWindow;
import org.apache.log4j.BasicConfigurator;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        BasicConfigurator.configure();
        new MainWindow();
    }

}
