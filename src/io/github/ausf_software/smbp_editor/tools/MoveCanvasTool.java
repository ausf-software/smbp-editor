package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.core.*;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.awt.*;

@EditorTool(name = "Move canvas")
public class MoveCanvasTool extends AbstractEditorTool {

    private Point last = null;
    private Editor editor= Editor.INSTANCE;

    @EditorToolAction(name = "start", hotKey = "SPACE")
    public void pressed() {
        Editor.INSTANCE.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        Point cur = MouseInfo.getPointerInfo().getLocation();
        if (!cur.equals(last)) {
            if (last != null) {
                // Ограничение смещения холста
                int dx = cur.x - last.x;
                int dy = cur.y - last.y;
                if (    editor.getCurrentCanvasX() + dx > -editor.MAX_VIEW_OVER_CANVAS - editor.getWidth() * (editor.getScale()/100) &&
                        editor.getCurrentCanvasY() + dy > -editor.MAX_VIEW_OVER_CANVAS - editor.getHeight() * (editor.getScale()/100) &&
                        editor.getCurrentCanvasX()  + dx < editor.MAX_VIEW_OVER_CANVAS &&
                        editor.getCurrentCanvasY() +  dy < editor.MAX_VIEW_OVER_CANVAS)
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
        Editor.INSTANCE.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

}
