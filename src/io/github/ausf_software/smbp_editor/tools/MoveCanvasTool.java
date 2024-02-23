package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.core.tool.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.tool.EditorTool;
import io.github.ausf_software.smbp_editor.core.tool.EditorToolAction;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.awt.*;

@EditorTool(name = "Move canvas")
public class MoveCanvasTool extends AbstractEditorTool {

    private Point last = null;

    @EditorToolAction(name = "start", hotKey = "SPACE")
    public void pressed() {
        EDITOR.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        Point cur = MouseInfo.getPointerInfo().getLocation();
        if (!cur.equals(last)) {
            if (last != null) {
                // Ограничение смещения холста
                int dx = cur.x - last.x;
                int dy = cur.y - last.y;
                if (    EDITOR.getCurrentCanvasX() + dx > -EDITOR.MAX_VIEW_OVER_CANVAS - EDITOR.getWidth() * (EDITOR.getScale()/100) &&
                        EDITOR.getCurrentCanvasY() + dy > -EDITOR.MAX_VIEW_OVER_CANVAS - EDITOR.getHeight() * (EDITOR.getScale()/100) &&
                        EDITOR.getCurrentCanvasX() + dx <  EDITOR.MAX_VIEW_OVER_CANVAS &&
                        EDITOR.getCurrentCanvasY() + dy <  EDITOR.MAX_VIEW_OVER_CANVAS)
                {
                    Editor.INSTANCE.moveCanvasCords(dx, dy);
                }
            }
            last = cur;
        }
    }

    @EditorToolAction(name = "end", hotKey = "released SPACE")
    public void release() {
        last = null;
        EDITOR.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

}
