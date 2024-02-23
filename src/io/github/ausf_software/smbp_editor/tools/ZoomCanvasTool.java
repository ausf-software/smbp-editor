package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.core.system.RenderOverCanvasViewportManager;
import io.github.ausf_software.smbp_editor.core.tool.*;
import io.github.ausf_software.smbp_editor.input.ListenerType;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.awt.*;

@EditorTool(name = "Zoom canvas tool")
public class ZoomCanvasTool extends AbstractEditorTool {

    public final int MaxZoom = 150;
    public final int MinZoom = 1;
    public final int ZoomStep = 5;

    @EditorToolAction(name = "zoom plus", hotKey = "mouseWheelUp",
            listenerType = ListenerType.MOUSE_WHEEL)
    public void zoomPlus() {
        if (EDITOR.getScale() + ZoomStep <= MaxZoom)
            EDITOR.addZoom(ZoomStep);
        RenderOverCanvasViewportManager.INSTANCE.requestRender("Current zoom hint");
    }

    @EditorToolAction(name = "zoom minus", hotKey = "mouseWheelDown",
            listenerType = ListenerType.MOUSE_WHEEL)
    public void zoomMinus() {
        if (EDITOR.getScale() - ZoomStep >= MinZoom)
            EDITOR.addZoom(-ZoomStep);
        RenderOverCanvasViewportManager.INSTANCE.requestRender("Current zoom hint");
    }

    @ToolRenderOverCanvasViewport(name = "Current zoom hint", timeRender = 3000)
    public static class ZoomCanvasHint extends RenderOverCanvasViewport {

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            int lineWidth = 200;
            int lineStroke = 2;
            int lineX = Editor.INSTANCE.SIZE_LINER + 30;
            int lineY = Editor.INSTANCE.SIZE_LINER + 20;
            g.drawLine(lineX, lineY, lineX + lineWidth, lineY);

            ZoomCanvasTool zoomCanvasTool = new ZoomCanvasTool();
            int rectX = lineX + (int)((float)(Editor.INSTANCE.getScale()) / zoomCanvasTool.MaxZoom * lineWidth);
            int rectY = lineY - 7;
            g.fillRect(rectX, rectY, 6,14);
            g.drawString(Editor.INSTANCE.getScale() + "%", lineX, lineY + 20);
        }
    }

}
