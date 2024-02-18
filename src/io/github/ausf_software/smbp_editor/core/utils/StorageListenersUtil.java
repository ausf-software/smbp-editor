package io.github.ausf_software.smbp_editor.core.utils;

import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

public class StorageListenersUtil {

    private static final Logger log = LoggerFactory.getLogger(StorageListenersUtil.class);

    public static Set<Method> getStorageListenerMethods(Reflections reflections) {
        return reflections.getMethodsAnnotatedWith(StorageListener.class);
    }

    public static boolean isStorageListener(Method m) {
        return m.isAnnotationPresent(StorageListener.class);
    }

    public static String[] getValuesNames(Method m) {
        if (isStorageListener(m))
            return m.getAnnotation(StorageListener.class).names();
        return new String[] {};
    }

    public static boolean isAnnotationMethod(Method m) {
        if (!EditorToolUtil.isToolClass(m.getDeclaringClass())) {
            log.info("Метод {} находится в классе, который не помечен в качестве инструмента " +
                    "редактора.", m.getName());
            return false;
        }
        return true;
    }
}
