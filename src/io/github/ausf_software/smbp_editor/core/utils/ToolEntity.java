package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.EditorTool;
import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;

import java.lang.reflect.Method;
import java.util.Set;

public class ToolEntity {

    private String toolName;
    private String icon;
    private Class<?> clazz;
    private EditorTool annotation;
    private Set<Method> actions;
    private Set<Method> storageListeners;
    private Set<Class<RenderOverCanvasViewport>> renderOverCanvasViewport;

    public ToolEntity(String toolName, String icon, Class<?> clazz, EditorTool annotation,
                      Set<Method> actions, Set<Method> storageListeners,
                      Set<Class<RenderOverCanvasViewport>> renderOverCanvasViewport) {
        this.toolName = toolName;
        this.icon = icon;
        this.clazz = clazz;
        this.annotation = annotation;
        this.actions = actions;
        this.storageListeners = storageListeners;
        this.renderOverCanvasViewport = renderOverCanvasViewport;
    }

    public String getIcon() {
        return icon;
    }

    public Set<Method> getStorageListeners() {
        return storageListeners;
    }

    public EditorTool getAnnotation() {
        return annotation;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getToolName() {
        return toolName;
    }

    public Set<Method> getActions() {
        return actions;
    }

    public Set<Class<RenderOverCanvasViewport>> getRenderOverCanvasViewport() {
        return renderOverCanvasViewport;
    }
}
