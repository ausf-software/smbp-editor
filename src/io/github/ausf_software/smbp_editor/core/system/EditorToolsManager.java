package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.EditorTool;
import io.github.ausf_software.smbp_editor.core.EditorToolAction;
import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.utils.*;
import io.github.ausf_software.smbp_editor.input.ActionInputMap;
import io.github.ausf_software.smbp_editor.input.MouseWheelStroke;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EditorToolsManager {

    private static final Logger log = LoggerFactory.getLogger(EditorToolsManager.class);

    private final HashMap<String, AbstractEditorTool>   TOOL_OBJECT;
    private final HashMap<String, ToolEntity>           TOOL_ENTITIES;
    private final HashMap<String, Boolean>              TOOL_ENABLE;
    private final ArrayList<String>                     TOOL_NAMES;

    private ActionInputMap actionInputMap = new ActionInputMap();

    private final SystemManager SYSTEM;

    public EditorToolsManager(SystemManager systemManager) {
        SYSTEM = systemManager;
        TOOL_OBJECT = new HashMap<>();
        TOOL_ENTITIES = new HashMap<>();
        TOOL_ENABLE = new HashMap<>();
        TOOL_NAMES = new ArrayList<>();
    }

    public void enableTool(String name) {
        ToolEntity entity = TOOL_ENTITIES.get(name);
        if (entity == null) return;
        try {
            // создание экземпляра инструмента
            AbstractEditorTool toolObject = (AbstractEditorTool) entity.getClazz().newInstance();
            TOOL_OBJECT.put(name, toolObject);
            // регистрация InputAction
            for (Method m : entity.getActions()) {
                EditorToolAction mDescription = m.getAnnotation(EditorToolAction.class);
                String key = EditorToolUtil.calcActionKeyName(entity.getAnnotation(), mDescription);
                addInput(mDescription, key);
                actionInputMap.addAction(key,
                        EditorToolUtil.toAction(new MethodPair(toolObject, m)));
            }
            // регистрируем слушатели хранилища
            SYSTEM.registerStorageListeners(toolObject, entity);
            // регистрируем рендер поверх холста
            SYSTEM.registerRenderOverCanvas(entity);
            // проверка для добавление на панель инструментов
            if (!entity.getIcon().equals(""))
                SYSTEM.addToolToPanel(entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Для инструмента {} не была найдена иконка по адресу {}. Добавление на " +
                            "панель инструментов будет проигнорировано.", entity.getToolName(),
                    entity.getIcon());
        }
    }

    public void loadToolEntities(Reflections reflections) {
        Set<Class<?>> tools = EditorToolUtil.getEditorTools(reflections);

        for (Class<?> tool : tools) {
            ToolEntity entity = extractTool(tool);
            if (entity != null) {
                TOOL_ENTITIES.put(entity.getToolName(), entity);
                TOOL_NAMES.add(entity.getToolName());
                TOOL_ENABLE.put(entity.getToolName(), false);
            }
        }
    }

    private ToolEntity extractTool(Class<?> tool) {
        if (!EditorToolUtil.isToolClass(tool))
            return null;

        Set<Method> actions = EditorToolUtil.getEditorToolActions(tool);
        if (actions.isEmpty()) {
            log.info("Класс {} не содержит методов событий.", tool.getName());
            return null;
        }

        EditorTool toolAnnotation = tool.getAnnotation(EditorTool.class);

        Set<Class<RenderOverCanvasViewport>> renderOver = new HashSet<>();
        Class<?>[] r = tool.getDeclaredClasses();
        for (Class<?> c : r)
            if (RenderOverCanvasViewportUtil.isRenderOverCanvasClass(c))
                renderOver.add((Class<RenderOverCanvasViewport>) c);
        return new ToolEntity(toolAnnotation.name(), toolAnnotation.icon(), tool, toolAnnotation,
                actions, StorageListenersUtil.getStorageListenerMethods(tool), renderOver);
    }

    private void addInput(EditorToolAction actionAnnotation, String key) {
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.KEY)
            actionInputMap.addInput(KeyStroke.getKeyStroke(actionAnnotation.hotKey()), key);
        if (actionAnnotation.listenerType() == EditorToolAction.ListenerType.MOUSE_WHEEL)
            actionInputMap.addMouseWheelInput(MouseWheelStroke.getMouseWheelStroke(actionAnnotation.hotKey()), key);
    }

    public ActionInputMap getActionInputMap() {
        return actionInputMap;
    }

    public AbstractEditorTool getToolObject(String name) {
        return TOOL_OBJECT.get(name);
    }

    public ArrayList<String> getToolNames() {
        return TOOL_NAMES;
    }
}
