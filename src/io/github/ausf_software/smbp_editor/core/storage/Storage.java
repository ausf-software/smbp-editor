package io.github.ausf_software.smbp_editor.core.storage;

import io.github.ausf_software.smbp_editor.core.utils.MethodPair;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {

    public Storage() {}

    private Map<String, Serializable> store = new HashMap<>();
    private Map<String, List<MethodPair>> methods = new HashMap<>();

    public void upload(String name, Serializable value) {
        store.put(name, value);
        triggered(name);
    }

    private void triggered(String name) {
        List<MethodPair> list = methods.get(name);
        if (list == null) return;
        for (MethodPair m : list) {
            StorageListener s = m.getMethod().getAnnotation(StorageListener.class);
            List<Object> args = new ArrayList<>();
            for (String str : s.names()) {
                args.add(store.get(str));
            }
            try {
                m.invoke(args.toArray());
            } catch (InvocationTargetException e) {
                System.out.println("[Storage] Проверьте на соответствие типы данных: "
                        + m.getMethod().getName());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerMethod(String name, MethodPair met) {
        if (!store.containsKey(name))
            System.out.println("[Storage] Не найдена запись с именем: " + name);
        if (!methods.containsKey(name))
            methods.put(name, new ArrayList<>());
        methods.get(name).add(met);
    }

    public void registerMethod(String[] names, MethodPair met) {
        for (String name : names) {
            registerMethod(name, met);
        }
    }

}
