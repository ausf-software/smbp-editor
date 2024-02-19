package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.ToolRenderOverCanvasViewport;

public class RenderOverCanvasViewportUtil {

    public static boolean isRenderOverCanvasClass(Class<?> tool) {
        return tool.getSuperclass().equals(RenderOverCanvasViewport.class)
                && tool.isAnnotationPresent(ToolRenderOverCanvasViewport.class);
    }

}
