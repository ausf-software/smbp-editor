package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.core.storage.Storage;
import io.github.ausf_software.smbp_editor.core.utils.StorageListenersUtil;
import io.github.ausf_software.smbp_editor.render.Editor;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class SystemManager {

    private static final String CLASSES_PATH = "io.github.ausf_software.smbp_editor";
    private final Reflections reflections = new Reflections(CLASSES_PATH);
    private final Storage storage = new Storage();
    private final EditorToolsManager editorToolsManager = new EditorToolsManager();

    public SystemManager() {
        editorToolsManager.load(storage, reflections);
    }

    public void registerInputMapsToEditor(Editor editor) {
        editor.registerInputMap(editorToolsManager.getActionInputMap().getInputMap());
        editor.registerActionMap(editorToolsManager.getActionInputMap().getActionMap());
        editor.registerMouseWheelMap(editorToolsManager.getActionInputMap().getMouseWheelMap());
    }

    private void loadStorageListener() {
        Set<Method> methods = StorageListenersUtil.getStorageListenerMethods(reflections);
        for (Method m : methods) {
            if (StorageListenersUtil.isAnnotationMethod(m))
                storage.registerMethod(StorageListenersUtil.getValuesNames(m), new MethodPair(m,
                        null));
        }
    }
}
