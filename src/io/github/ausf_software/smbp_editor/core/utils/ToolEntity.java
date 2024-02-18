package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;

import java.lang.reflect.Method;
import java.util.Set;

public class ToolEntity {

    private String toolName;
    private Set<Method> actions;
    private Set<RenderOverCanvasViewport> renderOverCanvasViewport;

    public ToolEntity(String toolName, Set<Method> actions,
                      Set<RenderOverCanvasViewport> renderOverCanvasViewport) {
        this.toolName = toolName;
        this.actions = actions;
        this.renderOverCanvasViewport = renderOverCanvasViewport;
    }

    public String getToolName() {
        return toolName;
    }

    public Set<Method> getActions() {
        return actions;
    }

    public Set<RenderOverCanvasViewport> getRenderOverCanvasViewport() {
        return renderOverCanvasViewport;
    }
}
