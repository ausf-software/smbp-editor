package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.storage.Storage;
import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import io.github.ausf_software.smbp_editor.core.tool.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.tool.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.core.utils.RenderOverCanvasViewportUtil;
import io.github.ausf_software.smbp_editor.render.Editor;
import io.github.ausf_software.smbp_editor.render.ToolsPanel;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;

public class SystemManager {

    private static final Logger log = LoggerFactory.getLogger(SystemManager.class);

    private static final String CLASSES_PATH = "io.github.ausf_software.smbp_editor.tools";
    private final Reflections reflections = new Reflections(CLASSES_PATH);
    private final Storage storage = new Storage();
    private final EditorToolsManager editorToolsManager;

    private final ToolsPanel toolsPanel = new ToolsPanel(this);
    private final Editor editor = Editor.INSTANCE;

    private Set<Class<RenderOverCanvasViewport>> renderOver;

    public SystemManager() {
        storage.upload("current_tool", "null");
        editorToolsManager = new EditorToolsManager(this);
        renderOver = RenderOverCanvasViewportUtil.getRenderOverCanvas(reflections);
        int count = editorToolsManager.getLoadingItemCount(reflections) + renderOver.size();
        log.info("Обнаружено элементов для загрузки: {}", count);
        editorToolsManager.loadToolEntities();
        for (String name : editorToolsManager.getToolNames()) {
            editorToolsManager.enableTool(name);
        }
        // регистрируем рендер поверх холста
        registerRenderOverCanvas(renderOver);
    }

    public void registerInputMapsToEditor(Editor editor) {
        editor.registerActionInputMap(editorToolsManager.getActionInputMap());
    }

    void addToolToPanel(ToolEntity entity) throws IOException {
        toolsPanel.addTool(entity.getIcon(), entity.getToolName());
    }

    public void removeToolToPanel(ToolEntity entity) {
        toolsPanel.removeTool(entity.getToolName());
    }

    public void changeTool(String name) {
        storage.upload("current_tool", name);
    }

    void registerStorageListeners(AbstractEditorTool tool, ToolEntity entity) {
        for (Method m : entity.getStorageListeners()) {
            MethodPair methodPair = new MethodPair(tool, m);
            for (String str : m.getAnnotation(StorageListener.class).names()) {
                storage.registerMethod(str, methodPair);
            }
        }
    }

    void removeStorageListeners(AbstractEditorTool tool, ToolEntity entity) {
        for (Method m : entity.getStorageListeners()) {
            MethodPair methodPair = new MethodPair(tool, m);
            for (String str : m.getAnnotation(StorageListener.class).names()) {
                storage.removeMethod(str, methodPair);
            }
        }
    }

    private void registerRenderOverCanvas(Set<Class<RenderOverCanvasViewport>> renderOver) {
        for (Class<RenderOverCanvasViewport> c : renderOver) {
            RenderOverCanvasViewportManager.INSTANCE.add(c);
        }
    }

    public ToolsPanel getToolsPanel() {
        return toolsPanel;
    }

    public Editor getEditor() {
        return editor;
    }
}
