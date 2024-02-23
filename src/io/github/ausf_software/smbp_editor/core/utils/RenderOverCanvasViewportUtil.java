package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.tool.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.tool.ToolRenderOverCanvasViewport;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class RenderOverCanvasViewportUtil {

    public static boolean isRenderOverCanvasClass(Class<?> tool) {
        return tool.getSuperclass().equals(RenderOverCanvasViewport.class)
                && tool.isAnnotationPresent(ToolRenderOverCanvasViewport.class);
    }

    public static Set<Class<RenderOverCanvasViewport>> getRenderOverCanvas(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(ToolRenderOverCanvasViewport.class)
                .stream()
                .map(input -> (Class<RenderOverCanvasViewport>) input)
                .collect(Collectors.toSet());
    }

}
