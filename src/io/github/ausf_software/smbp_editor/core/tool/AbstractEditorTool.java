package io.github.ausf_software.smbp_editor.core.tool;

import io.github.ausf_software.smbp_editor.core.storage.StorageListener;
import io.github.ausf_software.smbp_editor.render.Editor;

/**
 * Абстрактный класс служащий для задания методов которые должны
 * содержаться у каждого класса помеченным аннотацией <code>EditorTool</code>.
 * <p>Автоматически отслеживает выбран ли данный инструмент на панели инструментов.
 * Для получения информации о том, выбран ли данный инструмент используйте переменную
 * <code>currently</code>. Для определения действий которые необходимо сделать
 * при выборе данного инструмента и переключении на другой инструмент - переопределите
 * методы <code>enable</code> и <code>disable</code> соответственно.
 * @see StorageListener
 * @see io.github.ausf_software.smbp_editor.core.storage.Storage
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
public abstract class AbstractEditorTool {

    private String toolName;
    /**
     * Если true то данный инструмент выбран на панели инструментов.
     */
    protected boolean currently = false;

    /**
     * Редактор к которому прикреплен инструмент
     */
    public Editor EDITOR;

    /**
     * Устанавливает к какому редактору прикреплен инструмент
     * @param EDITOR редактор к которому прикреплен инструмент
     */
    public final void setEditor(Editor editor) {
        this.EDITOR = editor;
    }

    /**
     * Отслеживает переключения инструментов на панели инструментов.
     * @param name имя инструмента который сейчас выбран
     */
    @StorageListener(names = { "current_tool" })
    public final void listenChangeTool(String name) {
        if (name.equals(toolName)) {
            if (!currently) {
                currently = true;
                enable();
            }
        } else {
            if (currently) {
                currently = false;
                disable();
            }
        }
    }

    /**
     * Метод который будет вызван при выборе данного инструмента
     * на панели инструментов
     */
    protected void enable() { }

    /**
     * Метод который будет вызван при переключении на другой инструмент
     */
    protected void disable() { }

    /**
     * Устанавливает имя для объекта инструмента. Необходим
     * для отслеживания изменения текущего инструмента.
     * Автоматически вызывается управляющим инструментами при
     * включении инструмента.
     * @param toolName имя данного инструмента
     */
    public final void setToolName(String toolName) {
        this.toolName = toolName;
    }

    /**
     * Возвращает имя данного инструмента
     * @return имя данного инструмента
     */
    public final String getToolName() {
        return toolName;
    }
}
