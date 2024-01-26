package io.github.ausf_software.smbp_editor.core;

import io.github.ausf_software.smbp_editor.input.MouseWheelStroke;
import org.reflections.Reflections;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EditorToolsManager {

    public static EditorToolsManager INSTANCE = new EditorToolsManager();

    private HashMap<KeyStroke, Object> inputMap = new HashMap<>();
    private HashMap<Object, AbstractAction> actionMap = new HashMap<>();
    private HashMap<MouseWheelStroke, String> mouseWheelMap = new HashMap<>();

    public List<AbstractEditorTool> toolObjects = new ArrayList<>();

    private EditorToolsManager() {}

    public void load() {
        Reflections reflections = new Reflections("io.github.ausf_software.smbp_editor");
        Set<Class<?>> tools = reflections.getTypesAnnotatedWith(EditorTool.class);

        for (Class<?> tool : tools) {
            if (!isEditorToolClass(tool))
                continue;

            Set<Method> actions = getEditorToolActions(tool);
            if (actions.isEmpty())
                continue;

            try {
                AbstractEditorTool toolObject = (AbstractEditorTool) tool.newInstance();
                toolObjects.add(toolObject);
                EditorTool toolAnnotation = tool.getAnnotation(EditorTool.class);
                for (Method m : actions) {
                    EditorToolAction mDescription = m.getAnnotation(EditorToolAction.class);
                    String key = calcActionKeyName(toolAnnotation, mDescription);
                    addNewInput(mDescription, key);
                    actionMap.put(key, toAction(toolObject, m));
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            registerNewRenderOverCanvas(tool);
        }
    }

    private void registerNewRenderOverCanvas(Class<?> tool) {
        Class<?>[] r = tool.getDeclaredClasses();
        for (Class<?> c : r)
            if (c.isAnnotationPresent(ToolRenderOverCanvasViewport.class)
                && isRenderOverCanvasClass(c))
                RenderOverCanvasViewportManager.INSTANCE.put(c);
    }

    private void addNewInput(EditorToolAction actionAnnotation, String key) {
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.KEY)
            inputMap.put(KeyStroke.getKeyStroke(actionAnnotation.hotKey()), key);
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.MOUSE_WHEEL)
            mouseWheelMap.put(MouseWheelStroke.getMouseWheelStroke(actionAnnotation.hotKey()), key);
    }

    private Set<Method> getEditorToolActions(Class<?> tool) {
        Set<Method> actions = new HashSet<>();

        for (Method m : tool.getDeclaredMethods())
            if (m.isAnnotationPresent(EditorToolAction.class))
                actions.add(m);

        return actions;
    }

    private boolean isEditorToolClass(Class<?> tool) {
        return tool.getSuperclass().equals(AbstractEditorTool.class);
    }

    private boolean isRenderOverCanvasClass(Class<?> tool) {
        return tool.getSuperclass().equals(RenderOverCanvasViewport.class);
    }

    private String calcActionKeyName(EditorTool tool, EditorToolAction actionAnnotation) {
        return tool.name() + ": " + actionAnnotation.name();
    }

    private AbstractAction toAction(AbstractEditorTool tool, Method method) {
        return new AbstractAction() {
            private AbstractEditorTool editorTool = tool;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    method.invoke(editorTool);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
        };
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
