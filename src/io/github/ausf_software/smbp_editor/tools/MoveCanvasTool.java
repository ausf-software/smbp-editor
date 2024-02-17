package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.core.*;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.awt.*;

@EditorTool(name = "Move canvas")
public class MoveCanvasTool extends AbstractEditorTool {

    private Point last = null;

    @EditorToolAction(name = "start", hotKey = "SPACE")
    public void pressed() {
        Editor.INSTANCE.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        Point cur = MouseInfo.getPointerInfo().getLocation();
        if (!cur.equals(last)) {
            if (last != null) {
                // Ограничение смещения холста
                if (cur.getX() >- Editor.INSTANCE.MAX_VIEW_OVER_CANVAS &&
                        cur.getX() < Editor.INSTANCE.MAX_VIEW_OVER_CANVAS+Editor.INSTANCE.getWidth() &&
                        cur.getY() >- Editor.INSTANCE.MAX_VIEW_OVER_CANVAS &&
                        cur.getY() < Editor.INSTANCE.MAX_VIEW_OVER_CANVAS+Editor.INSTANCE.getHeight()) {
                    Editor.INSTANCE.moveCanvasCords(cur.x - last.x, cur.y - last.y);
                }
                // Округление смещения до граничного допустимого значения, если смещение превышает и пространство за холстом
                else {
                    Point New_cur = MouseInfo.getPointerInfo().getLocation();
                    New_cur.x = (int)Math.min(Math.max(cur.getX(), -Editor.INSTANCE.MAX_VIEW_OVER_CANVAS),
                            Editor.INSTANCE.MAX_VIEW_OVER_CANVAS + Editor.INSTANCE.getWidth());
                    New_cur.y = (int)Math.min(Math.max(cur.getY(), -Editor.INSTANCE.MAX_VIEW_OVER_CANVAS),
                            Editor.INSTANCE.MAX_VIEW_OVER_CANVAS + Editor.INSTANCE.getHeight());
                    Editor.INSTANCE.moveCanvasCords(New_cur.x - last.x, New_cur.y - last.y);
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
