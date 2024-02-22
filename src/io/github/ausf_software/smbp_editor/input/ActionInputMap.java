package io.github.ausf_software.smbp_editor.input;

import javax.swing.*;
import java.util.HashMap;

/**
 * Класс объединяющий карты нажатий на клавиши клавиатуры,
 * прокрутки колесика мыши, карты событий.
 */
public class ActionInputMap {

    private HashMap<KeyStroke, Object> keyMap = new HashMap<>();
    private HashMap<Object, AbstractAction> actionMap = new HashMap<>();
    private HashMap<MouseWheelStroke, String> mouseWheelMap = new HashMap<>();

    /**
     * Добавляет новое событие для отслеживания
     * @param type тип события
     * @param input строковое представление события
     * @param key ключ действия
     */
    public void addInput(ListenerType type, String input, String key) {
        if (type == ListenerType.KEY)
            addKeyInput(KeyStroke.getKeyStroke(input), key);
        if (type == ListenerType.MOUSE_WHEEL)
            addMouseWheelInput(MouseWheelStroke.getMouseWheelStroke(input), key);
    }

    /**
     * Удаляет зарегистрированное событие, а также действие,
     * которое ему соответствует.
     * <p>В случае если необходимо оставить зарегистрированное
     * действие, в качестве ключа необходимо указать null.
     * @param type тип события
     * @param input строковое представление события
     * @param key ключ действия
     */
    public void removeInput(ListenerType type, String input, String key) {
        if (type == ListenerType.KEY)
            removeKeyInput(KeyStroke.getKeyStroke(input));
        if (type == ListenerType.MOUSE_WHEEL)
            removeMouseWheelInput(MouseWheelStroke.getMouseWheelStroke(input));
        removeAction(key);
    }

    /**
     * Удаляет зарегистрированное событие прокрутки колесика мыши.
     * @param mouseWheelStroke описание события прокрутки колесика мыши
     */
    private void removeMouseWheelInput(MouseWheelStroke mouseWheelStroke) {
        mouseWheelMap.remove(mouseWheelStroke);
    }

    /**
     * Удаляет зарегистрированное событие нажатия клавиши на клавиатуре.
     * @param keyStroke описание события нажатия клавиши на клавиатуре
     */
    private void removeKeyInput(KeyStroke keyStroke) {
        keyMap.remove(keyStroke);
    }

    /**
     * Удаляет действие
     * @param key ключ действия
     */
    private void removeAction(String key) {
        if (key == null) return;
            actionMap.remove(key);
    }

    /**
     * Добавляет событие прокрутки колесика мыши
     * @param mouseWheelStroke описание события прокрутки колесика мыши
     * @param key ключ действия
     */
    public void addMouseWheelInput(MouseWheelStroke mouseWheelStroke, String key) {
        mouseWheelMap.put(mouseWheelStroke, key);
    }

    /**
     * Добавляет действие
     * @param key ключ действия
     * @param abstractAction объект действия
     */
    public void addAction(String key, AbstractAction abstractAction) {
        actionMap.put(key, abstractAction);
    }

    /**
     * Добавляет событие нажатия клавиши на клавиатуре.
     * @param keyStroke описание события нажатия клавиши на клавиатуре
     * @param key ключ события
     */
    public void addKeyInput(KeyStroke keyStroke, String key) {
        keyMap.put(keyStroke, key);
    }

    /**
     * Возвращает карту событий нажатия на клавиатуру
     * @return карту событий нажатия на клавиатуру
     */
    public HashMap<KeyStroke, Object> getKeyMap() {
        return keyMap;
    }

    /**
     * Возвращает карту действий
     * @return карту действий
     */
    public HashMap<Object, AbstractAction> getActionMap() {
        return actionMap;
    }

    /**
     * Возвращает карту событий прокрутки колесика мыши
     * @return карту событий прокрутки колесика мыши
     */
    public HashMap<MouseWheelStroke, String> getMouseWheelMap() {
        return mouseWheelMap;
    }
}
