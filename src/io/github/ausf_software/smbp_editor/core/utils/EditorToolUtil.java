package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.tool.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.tool.EditorTool;
import io.github.ausf_software.smbp_editor.core.tool.EditorToolAction;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class EditorToolUtil {
    private static final Logger log = LoggerFactory.getLogger(EditorToolUtil.class);

    public static boolean isToolClass(Class<?> clazz) {
        if (!(clazz.isAnnotationPresent(EditorTool.class)
                && clazz.getSuperclass().equals(AbstractEditorTool.class))) {
            log.info("Класс {} не помечен в качестве инструмента, либо не унаследован от " +
                            "AbstractEditorTool.", clazz.getName());
            return false;
        }
        return true;
    }

    public static Set<Class<?>> getEditorTools(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(EditorTool.class);
    }

    public static Set<Method> getEditorToolActions(Class<?> tool) {
        Set<Method> actions = new HashSet<>();

        for (Method m : tool.getDeclaredMethods())
            if (m.isAnnotationPresent(EditorToolAction.class))
                actions.add(m);

        return actions;
    }

    public static AbstractAction toAction(MethodPair methodPair) {
        return new AbstractAction() {
            private MethodPair editorToolAction = methodPair;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editorToolAction.invoke();
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                } catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
        };
    }

    public static String calcActionKeyName(EditorTool tool, EditorToolAction actionAnnotation) {
        return tool.name() + ": " + actionAnnotation.name();
    }

}
