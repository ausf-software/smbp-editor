package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.*;
import io.github.ausf_software.smbp_editor.core.storage.Storage;
import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import io.github.ausf_software.smbp_editor.core.utils.EditorToolUtil;
import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.core.utils.ToolEntity;
import io.github.ausf_software.smbp_editor.input.ActionInputMap;
import io.github.ausf_software.smbp_editor.input.MouseWheelStroke;
import io.github.ausf_software.smbp_editor.render.ToolsPanel;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class EditorToolsManager {

    private static final Logger log = LoggerFactory.getLogger(EditorToolsManager.class);

    private HashMap<String, AbstractEditorTool> toolObjects = new HashMap<>();
    private HashMap<String, ToolEntity> toolEntities = new HashMap<>();
    private ActionInputMap actionInputMap = new ActionInputMap();

    public EditorToolsManager() {}

    public void load(Storage storage, Reflections reflections) {
        Set<Class<?>> tools = EditorToolUtil.getEditorTools(reflections);

        for (Class<?> tool : tools) {
            if (!EditorToolUtil.isToolClass(tool))
                continue;

            Set<Method> actions = EditorToolUtil.getEditorToolActions(tool);
            if (actions.isEmpty()) {
                log.info("Класс {} не содержит методов событий.", tool.getName());
                continue;
            }

            try {
                AbstractEditorTool toolObject = (AbstractEditorTool) tool.newInstance();
                EditorTool toolAnnotation = tool.getAnnotation(EditorTool.class);
                toolObjects.put(toolAnnotation.name(), toolObject);
                if (!toolAnnotation.icon().equals(""))
                    ToolsPanel.INSTANCE.addTool(toolAnnotation.icon(), toolAnnotation.name());
                for (Method m : actions) {
                    EditorToolAction mDescription = m.getAnnotation(EditorToolAction.class);
                    String key = EditorToolUtil.calcActionKeyName(toolAnnotation, mDescription);
                    addInput(mDescription, key);
                    actionInputMap.addAction(key,
                            EditorToolUtil.toAction(new MethodPair(toolObject, m)));
                }
                registerStorageListeners(toolObject, tool.getDeclaredMethods(), storage);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            registerRenderOverCanvas(tool);
        }
    }

    private void registerRenderOverCanvas(Class<?> tool) {
        Class<?>[] r = tool.getDeclaredClasses();
        for (Class<?> c : r)
            if (c.isAnnotationPresent(ToolRenderOverCanvasViewport.class)
                && isRenderOverCanvasClass(c))
                RenderOverCanvasViewportManager.INSTANCE.put(c);
    }

    private void registerStorageListeners(AbstractEditorTool tool, Method[] methods, Storage storage) {
        for (Method m : methods) {
            if (m.isAnnotationPresent(StorageListener.class)) {
                MethodPair methodPair = new MethodPair(tool, m);
                for (String str : m.getAnnotation(StorageListener.class).names()) {
                    storage.registerMethod(str, methodPair);
                }
            }
        }
    }

    private void addInput(EditorToolAction actionAnnotation, String key) {
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.KEY)
            actionInputMap.addInput(KeyStroke.getKeyStroke(actionAnnotation.hotKey()), key);
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.MOUSE_WHEEL)
            actionInputMap.addMouseWheelInput(MouseWheelStroke.getMouseWheelStroke(actionAnnotation.hotKey()), key);
    }

    private boolean isRenderOverCanvasClass(Class<?> tool) {
        return tool.getSuperclass().equals(RenderOverCanvasViewport.class);
    }

    public ActionInputMap getActionInputMap() {
        return actionInputMap;
    }

    public AbstractEditorTool getToolObject(String name) {
        return toolObjects.get(name);
    }

}
