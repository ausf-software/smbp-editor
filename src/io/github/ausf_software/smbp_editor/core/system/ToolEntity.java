package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.tool.ConfigurableField;
import io.github.ausf_software.smbp_editor.core.tool.EditorTool;
import io.github.ausf_software.smbp_editor.core.utils.EditorToolUtil;
import io.github.ausf_software.smbp_editor.core.utils.StorageListenersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс предоставляющий удобное хранение всей информации об инструменте редактора.
 * Содержит неизменяемые поля. Может быть создан только посредством вызова
 * фабричного метода <code>extractTool</code>.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
class ToolEntity {

    private static final Logger log = LoggerFactory.getLogger(ToolEntity.class);

    private final String toolName;
    private final String icon;
    private final Class<?> clazz;
    private final EditorTool annotation;
    private final Set<Method> actions;
    private final Set<Method> storageListeners;
    private final Set<Field> configField;
    private final String cfgName;

    /**
     /**
     * Создает экземпляр ToolEntity по заданным полям класса
     * @param toolName имя инструмента
     * @param icon путь к файлу иконки инструмента
     * @param clazz объект класса инструмента
     * @param annotation объект аннотации инструмента
     * @param actions множество методов действий инструмента
     * @param storageListeners множество методов слушателей хранилища
     * @param configField множество конфигурируемых полей класса
     * @param cfgName имя конфигурационного файла инструмента
     */
    private ToolEntity(String toolName, String icon, Class<?> clazz, EditorTool annotation,
                       Set<Method> actions, Set<Method> storageListeners,
                       Set<Field> configField, String cfgName) {
        this.toolName = toolName;
        this.icon = icon;
        this.clazz = clazz;
        this.annotation = annotation;
        this.actions = actions;
        this.storageListeners = storageListeners;
        this.configField = configField;
        this.cfgName = cfgName;
    }

    /**
     * Парсит указанный класс инструмента и возвращает его
     * представление в виде ToolEntity
     * @param tool класс для преобразования в EntityTool
     * @return представление в виде ToolEntity указанного класса
     */
    public static ToolEntity extractTool(Class<?> tool) {
        if (!EditorToolUtil.isToolClass(tool))
            return null;

        Set<Method> actions = EditorToolUtil.getEditorToolActions(tool);
        if (actions.isEmpty()) {
            log.info("Класс {} не содержит методов событий.", tool.getName());
            return null;
        }

        EditorTool toolAnnotation = tool.getAnnotation(EditorTool.class);

        Set<Field> fieldSet = List.of(tool.getFields())
                        .stream()
                        .filter(a -> a.isAnnotationPresent(ConfigurableField.class))
                        .collect(Collectors.toSet());

        return new ToolEntity(toolAnnotation.name(), toolAnnotation.icon(), tool, toolAnnotation,
                actions, StorageListenersUtil.getStorageListenerMethods(tool), fieldSet,
                toolAnnotation.cfg());
    }

    /**
     * Возвращает путь к файлу иконки инструмента
     * @return путь к файлу иконки инструмента
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Возвращает множество методов слушателей хранилища
     * @return множество методов слушателей хранилища
     */
    public Set<Method> getStorageListeners() {
        return storageListeners;
    }

    /**
     * Возвращает объект аннотации инструмента редактора
     * @return объект аннотации инструмента редактора
     */
    public EditorTool getAnnotation() {
        return annotation;
    }

    /**
     * Возвращает объект класса инструмента редактора
     * @return объект класса инструмента редактора
     */
    public Class<?> getEditorToolClass() {
        return clazz;
    }

    /**
     * Возвращает имя инструмента
     * @return имя инструмента
     */
    public String getToolName() {
        return toolName;
    }

    /**
     * Возвращает множество методов действий инструмента
     * @return множество методов действий инструмента
     */
    public Set<Method> getActions() {
        return actions;
    }

    /**
     * Возвращает множество полей инструмента,
     * значение которых находится в конфигурационном файле
     * @return множество полей инструмента, значение которых
     * находится в конфигурационном файле
     */
    public Set<Field> getConfigField() {
        return configField;
    }

    /**
     * Возвращает имя конфигурационного файла инструмента
     * @return имя конфигурационного файла инструмента
     */
    public String getCfgName() {
        return cfgName;
    }
}
