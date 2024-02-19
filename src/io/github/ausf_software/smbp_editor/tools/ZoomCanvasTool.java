package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.core.*;
import io.github.ausf_software.smbp_editor.core.system.RenderOverCanvasViewportManager;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.awt.*;

@EditorTool(name = "Zoom canvas tool")
public class ZoomCanvasTool extends AbstractEditorTool {

    private Editor editor;

    @EditorToolAction(name = "zoom plus", hotKey = "mouseWheelUp",
            listenerType = EditorToolAction.ListenerType.MOUSE_WHEEL)
    public void zoomPlus() {
        if (Editor.INSTANCE.getScale() + 5 <= 150)
            Editor.INSTANCE.addZoom(5);
        RenderOverCanvasViewportManager.INSTANCE.requestRender("Current zoom hint");
    }

    @EditorToolAction(name = "zoom minus", hotKey = "mouseWheelDown",
            listenerType = EditorToolAction.ListenerType.MOUSE_WHEEL)
    public void zoomMinus() {
        if (Editor.INSTANCE.getScale() - 5 >= 1)
            Editor.INSTANCE.addZoom(-5);
    }

    @ToolRenderOverCanvasViewport(name = "Current zoom hint", timeRender = 1000)
    public static class ZoomCanvasHint extends RenderOverCanvasViewport {

        public ZoomCanvasHint() {
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(Editor.INSTANCE.getSize().width / 2,
                    Editor.INSTANCE.getSize().height / 2,
                    50,50);
        }
    }

}
