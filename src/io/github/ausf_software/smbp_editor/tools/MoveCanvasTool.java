package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.window.panels.Editor;

import java.awt.*;

@EditorTool(name = "MoveCanvas")
public class MoveCanvasTool extends AbstractEditorTool {

    private Point last = null;

    @EditorToolAction(name = "start", hotKey = "SPACE")
    public void pressed() {
        Editor.INSTANCE.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        Point cur = MouseInfo.getPointerInfo().getLocation();
        if (!cur.equals(last)) {
            if (last != null)
                Editor.INSTANCE.moveCanvasCords(cur.x - last.x, cur.y - last.y);
        }
        last = cur;
    }

    @EditorToolAction(name = "end", hotKey = "released SPACE")
    public void release() {
        last = null;
        Editor.INSTANCE.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
