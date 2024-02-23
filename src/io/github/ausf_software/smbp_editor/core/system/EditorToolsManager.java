package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.tool.AbstractEditorTool;
import io.github.ausf_software.smbp_editor.core.tool.EditorToolAction;
import io.github.ausf_software.smbp_editor.core.utils.EditorToolUtil;
import io.github.ausf_software.smbp_editor.core.utils.MethodPair;
import io.github.ausf_software.smbp_editor.input.ActionInputMap;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Класс производящий загрузку классов инструментов, хранящий
 * всю информацию о них, производящий включение и отключение инструментов
 * по указанному имени.
 * @see SystemManager
 * @see ToolEntity
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
class EditorToolsManager {

    private static final Logger log = LoggerFactory.getLogger(EditorToolsManager.class);

    private final HashMap<String, AbstractEditorTool>   TOOL_OBJECT;
    private final HashMap<String, ToolEntity>           TOOL_ENTITIES;
    private final HashMap<String, Boolean>              TOOL_ENABLE;
    private final List<String>                          TOOL_NAMES;

    private ActionInputMap actionInputMap;

    /**
     * Ссылка на управляющего системой, к которому прикреплен
     * данный экземпляр
     */
    private final SystemManager SYSTEM;

    /**
     * Закешированные классы инструментов
     */
    private Set<Class<?>> tools;

    /**
     * Создает управляющего инструментами относящегося
     * к указанному управляющему системой
     * @param systemManager управляющий системой к которому выполняется привязка
     */
    public EditorToolsManager(SystemManager systemManager) {
        SYSTEM = systemManager;
        TOOL_OBJECT = new HashMap<>();
        TOOL_ENTITIES = new HashMap<>();
        TOOL_ENABLE = new HashMap<>();
        TOOL_NAMES = new ArrayList<>();
        actionInputMap = new ActionInputMap();
        tools = new HashSet<>();
    }

    /**
     * Индексирует аннотации относящиеся к инструменту, кеширует классы
     * инструментов для дальнейшего анализа, возвращает количество
     * элементов которые будут подгружаться.
     * @param reflections экземпляр для индексации аннотаций
     * @return возвращает количество подгружаемых элементов инструментов
     */
    public int getLoadingItemCount(Reflections reflections) {
        tools = EditorToolUtil.getEditorTools(reflections);
        int size = tools.size();
        for (Class<?> c : tools) {
            size += c.getDeclaredMethods().length;
        }
        return size;
    }

    /**
     * Производит создание экземпляра инструмента, добавляет его
     * события в ActionInputMap, добавляет инструмент на панель
     * инструментов.
     * @param name имя инструмента который необходимо включить
     */
    public void enableTool(String name) {
        ToolEntity entity = TOOL_ENTITIES.get(name);
        if (entity == null) return;
        try {
            // создание экземпляра инструмента
            AbstractEditorTool toolObject = (AbstractEditorTool) entity.getEditorToolClass().newInstance();
            toolObject.setToolName(name);
            toolObject.setEditor(SYSTEM.getEditor());
            TOOL_OBJECT.put(name, toolObject);
            // регистрация InputAction
            for (Method m : entity.getActions()) {
                EditorToolAction mDescription = m.getAnnotation(EditorToolAction.class);
                String key = EditorToolUtil.calcActionKeyName(entity.getAnnotation(), mDescription);
                actionInputMap.addInput(mDescription.listenerType(), mDescription.hotKey(), key);
                actionInputMap.addAction(key,
                        EditorToolUtil.toAction(new MethodPair(toolObject, m)));
            }
            // регистрируем слушатели хранилища
            SYSTEM.registerStorageListeners(toolObject, entity);
            // проверка для добавление на панель инструментов
            if (!entity.getIcon().equals(""))
                SYSTEM.addToolToPanel(entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Для инструмента {} не была найдена иконка по адресу {}. Добавление на " +
                            "панель инструментов будет проигнорировано.", entity.getToolName(),
                    entity.getIcon());
        }
        log.info("Инструмент \"{}\" включен", name);
    }

    /**
     * Производит удаление экземпляра инструмента, удаляет его
     * события в ActionInputMap, удаляет инструмент с панели
     * инструментов.
     * @param name имя инструмента который необходимо выключить
     */
    public void disableTool(String name) {
        ToolEntity entity = TOOL_ENTITIES.get(name);
        if (entity == null) return;
        // отключение событий в InputAction
        for (Method m : entity.getActions()) {
            EditorToolAction mDescription = m.getAnnotation(EditorToolAction.class);
            String key = EditorToolUtil.calcActionKeyName(entity.getAnnotation(), mDescription);
            actionInputMap.removeInput(mDescription.listenerType(), mDescription.hotKey(), key);
        }
        AbstractEditorTool toolObject = TOOL_OBJECT.get(name);
        // отключение слушателей хранилища
        SYSTEM.removeStorageListeners(toolObject, entity);
        // удаление экземпляра инструмента
        TOOL_OBJECT.remove(name);
        // проверка для добавление на панель инструментов
        if (!entity.getIcon().equals(""))
            SYSTEM.removeToolToPanel(entity);
        log.info("Инструмент \"{}\" выключен", name);
    }

    /**
     * Получает ToolEntity проиндексированных классов инструментов
     * и вносит их в базу для дальнейшей возможности включить
     * существующие инструменты.
     * <p>ВАЖНО: для корректной работы метода классы инструментов
     * должны были быть получены ранее. В случае если этого не произошло
     * метод сразу же завершит свое выполнение.
     */
    public void loadToolEntities() {
        if (tools.isEmpty()) return;
        for (Class<?> tool : tools) {
            ToolEntity entity = ToolEntity.extractTool(tool);
            if (entity != null) {
                TOOL_ENTITIES.put(entity.getToolName(), entity);
                TOOL_NAMES.add(entity.getToolName());
                TOOL_ENABLE.put(entity.getToolName(), false);
            }
        }
    }

    /**
     * Возвращает ссылку на включенные обработчики событий инструментов.
     * @return ссылку на включенные обработчики событий инструментов
     */
    public ActionInputMap getActionInputMap() {
        return actionInputMap;
    }

    /**
     * Возвращает список содержащий имена
     * всех корректно распознанных инструментов
     * @return список имен инструментов
     */
    public List<String> getToolNames() {
        return TOOL_NAMES;
    }
}
