package io.github.ausf_software.smbp_editor.tools;

import io.github.ausf_software.smbp_editor.window.panels.Editor;

@EditorTool(name = "ZoomCanvasTool")
public class ZoomCanvasTool extends AbstractEditorTool {

    private Editor editor;

    @EditorToolAction(name = "zoomPlus", hotKey = "mouseWheelUp",
            listenerType = EditorToolAction.ListenerType.MOUSE_WHEEL)
    public void zoomPlus() {
        if (Editor.INSTANCE.getScale() + 5 <= 150)
            Editor.INSTANCE.addZoom(5);
        System.out.println("scale" + Editor.INSTANCE.getScale());
    }

    @EditorToolAction(name = "zoomMinus", hotKey = "mouseWheelDown",
            listenerType = EditorToolAction.ListenerType.MOUSE_WHEEL)
    public void zoomMinus() {
        if (Editor.INSTANCE.getScale() - 5 >= 1)
            Editor.INSTANCE.addZoom(-5);
        System.out.println("scale" + Editor.INSTANCE.getScale());
    }

}