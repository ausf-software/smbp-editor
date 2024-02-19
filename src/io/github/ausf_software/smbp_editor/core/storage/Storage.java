package io.github.ausf_software.smbp_editor.core.storage;

import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Механизм хранилища предназначен для создания и отслеживания
 * изменений определенного значения, на которые подписаны
 * соответствующие методы.
 * <p>Доступ и запись к объектам в хранилище осуществляется с
 * использованием строкового идентификатора.
 * <p>Методы, которые должны выполняться при изменении значения,
 * помечаются аннотацией <code>StorageListener</code>.
 * @see StorageListener
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
public class Storage {

    private static final Logger log = LoggerFactory.getLogger(Storage.class);

    private Map<String, Serializable> store = new HashMap<>();
    private Map<String, List<MethodPair>> methods = new HashMap<>();

    /**
     * Загружает в хранилище значение с указанным именем,
     * после сообщает об этом всем слушателям.
     * @param name имя значения
     * @param value значение
     */
    public void upload(String name, Serializable value) {
        store.put(name, value);
        log.info("[Storage] Значение \"{}\" было изменено на \"{}\"", name, value);
        triggered(name);
    }

    /**
     * Сообщает всем слушателем значения, что
     * произошло изменение.
     * @param name имя значения
     */
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
                log.error("[Storage] Проверьте на соответствие типы данных: {}",
                        m.getMethod().getName());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Удаляет зарегистрированный в качестве слушателя метод.
     * @param name имя значения, к которому прикреплен метод
     * @param method метод для удаления
     */
    public void removeMethod(String name, MethodPair method) {
        if (!store.containsKey(name))
            return;
        methods.get(name).remove(method);
    }

    /**
     * Регистрирует метод в качестве слушателя.
     * @param name имя значения
     * @param met метод для регистрации
     */
    public void registerMethod(String name, MethodPair met) {
        if (!store.containsKey(name))
            log.error("[Storage] Не найдена запись с именем: {}", name);
        if (!methods.containsKey(name))
            methods.put(name, new ArrayList<>());
        methods.get(name).add(met);
    }

    /**
     * Регистрирует метод в качестве слушателя.
     * @param names массив с именами значений к которым регистрируется метод
     * @param met метод для регистрации
     */
    public void registerMethod(String[] names, MethodPair met) {
        for (String name : names) {
            registerMethod(name, met);
        }
    }

}
