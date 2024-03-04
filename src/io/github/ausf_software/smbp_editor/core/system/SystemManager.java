package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.storage.Storage;
import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import io.github.ausf_software.smbp_editor.core.tool.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.tool.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.utils.ConfigureToolObjectUtil;
import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.core.utils.RenderOverCanvasViewportUtil;
import io.github.ausf_software.smbp_editor.render.Editor;
import io.github.ausf_software.smbp_editor.render.LoadingWindow;
import io.github.ausf_software.smbp_editor.render.MainWindow;
import io.github.ausf_software.smbp_editor.render.ToolsPanel;
import io.github.ausf_software.smbp_editor.render.settings.SettingsWindow;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.List;

public class SystemManager {

    public static final String CFG_PATH = "res/";
    private static final Logger log = LoggerFactory.getLogger(SystemManager.class);

    private static final String CLASSES_PATH = "io.github.ausf_software.smbp_editor.tools";
    private final Reflections reflections = new Reflections(CLASSES_PATH);
    private final Storage storage = new Storage();
    private final EditorToolsManager editorToolsManager;

    private final ToolsPanel toolsPanel = new ToolsPanel(this);
    private final Editor editor = Editor.INSTANCE;

    private final MainWindow mainWindow;

    private Set<Class<RenderOverCanvasViewport>> renderOver;
    private LoadingWindow loadingWindow = new LoadingWindow();
    private SettingsWindow settingsWindow;

    private String[] enTools;

    public SystemManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        settingsWindow = new SettingsWindow(this);

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("smbp.cfg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> en = ConfigureToolObjectUtil.loadArray(properties, "enabledTools");
        enTools = en.toArray(new String[en.size()]);

        storage.upload("current_tool", "null");
        editorToolsManager = new EditorToolsManager(this);
        renderOver = RenderOverCanvasViewportUtil.getRenderOverCanvas(reflections);
        int count = editorToolsManager.getLoadingItemCount(reflections)
                        + renderOver.size() + enTools.length;
        log.info("Обнаружено элементов для загрузки: {}", count);
        loadingWindow.setCountLoadingItem(count);
        editorToolsManager.loadToolEntities();
        for (String name : enTools) {
            editorToolsManager.enableTool(name);
            loaded();
        }
        // регистрируем рендер поверх холста
        registerRenderOverCanvas(renderOver);

        loadingWindow.dispose();
        mainWindow.setVisible(true);
    }

    public void loaded() {
        loadingWindow.addProgress();
    }

    public void registerInputMapsToEditor(Editor editor) {
        editor.registerActionInputMap(editorToolsManager.getActionInputMap());
    }

    void addToolToPanel(ToolEntity entity) {
        toolsPanel.addTool(entity.getIcon(), entity.getToolName());
    }

    public void removeToolToPanel(ToolEntity entity) {
        toolsPanel.removeTool(entity.getToolName());
    }

    public void changeTool(String name) {
        storage.upload("current_tool", name);
    }

    public List<String> getAllToolNames() {
        return editorToolsManager.getToolNames();
    }

    public List<String> getEnabledToolNames() {
        return new ArrayList<>(List.of(enTools));
    }

    public Set<Field> getConfigureField(String name) {
        return editorToolsManager.getToolEntity(name).getConfigField();
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
            loaded();
        }
    }

    public ToolsPanel getToolsPanel() {
        return toolsPanel;
    }

    public Editor getEditor() {
        return editor;
    }
}
