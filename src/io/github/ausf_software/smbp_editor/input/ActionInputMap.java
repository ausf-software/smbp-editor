package io.github.ausf_software.smbp_editor.input;

import javax.swing.*;
import java.util.HashMap;

public class ActionInputMap {

    private HashMap<KeyStroke, Object> inputMap = new HashMap<>();
    private HashMap<Object, AbstractAction> actionMap = new HashMap<>();
    private HashMap<MouseWheelStroke, String> mouseWheelMap = new HashMap<>();

    public void addMouseWheelInput(MouseWheelStroke mouseWheelStroke, String key) {
        mouseWheelMap.put(mouseWheelStroke, key);
    }

    public void addAction(String key, AbstractAction abstractAction) {
        actionMap.put(key, abstractAction);
    }

    public void addInput(KeyStroke keyStroke, String key) {
        inputMap.put(keyStroke, key);
    }

    public HashMap<KeyStroke, Object> getInputMap() {
        return inputMap;
    }

    public HashMap<Object, AbstractAction> getActionMap() {
        return actionMap;
    }

    public HashMap<MouseWheelStroke, String> getMouseWheelMap() {
        return mouseWheelMap;
    }
}
