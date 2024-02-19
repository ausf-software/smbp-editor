package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.storage.Storage;
import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.core.utils.ToolEntity;
import io.github.ausf_software.smbp_editor.render.Editor;
import io.github.ausf_software.smbp_editor.render.ToolsPanel;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.Method;

public class SystemManager {

    private static final String CLASSES_PATH = "io.github.ausf_software.smbp_editor";
    private final Reflections reflections = new Reflections(CLASSES_PATH);
    private final Storage storage = new Storage();
    private final EditorToolsManager editorToolsManager;

    public SystemManager() {
        editorToolsManager = new EditorToolsManager(this);
        editorToolsManager.loadToolEntities(reflections);
        for (String name : editorToolsManager.getToolNames()) {
            editorToolsManager.enableTool(name);
        }
    }

    public void registerInputMapsToEditor(Editor editor) {
        editor.registerInputMap(editorToolsManager.getActionInputMap().getInputMap());
        editor.registerActionMap(editorToolsManager.getActionInputMap().getActionMap());
        editor.registerMouseWheelMap(editorToolsManager.getActionInputMap().getMouseWheelMap());
    }

    public void addToolToPanel(ToolEntity entity) throws IOException {
        ToolsPanel.INSTANCE.addTool(entity.getIcon(), entity.getToolName());
    }


    public void registerStorageListeners(AbstractEditorTool tool, ToolEntity entity) {
        for (Method m : entity.getStorageListeners()) {
            MethodPair methodPair = new MethodPair(tool, m);
            for (String str : m.getAnnotation(StorageListener.class).names()) {
                storage.registerMethod(str, methodPair);
            }
        }
    }

    public void registerRenderOverCanvas(ToolEntity tool) {
        for (Class<RenderOverCanvasViewport> c : tool.getRenderOverCanvasViewport())
            RenderOverCanvasViewportManager.INSTANCE.put(c);
    }

}
