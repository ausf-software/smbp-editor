package io.github.ausf_software.smbp_editor.core;

import io.github.ausf_software.smbp_editor.input.ListenerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация описывающая метод, который будет выполнен при
 * указанном событии. Метод индексируется при запуске программы
 * и регистрируется к соответствующему слушателю событий.
 * <p>Строка с именем необходима для более детального отображения
 * информации о инструменте, к которому приписан сей метод, в
 * окне настроек.
 * <p>Параметр <code>hotKey</code> содержит строковое описание события, которое
 * приведет у вызову данного метода.
 * <p>Параметр <code>listenerType</code> отвечает за тип слушателя события, к
 * которому будет прикрепляться данный метод.
 * @see io.github.ausf_software.smbp_editor.input.MouseWheelStroke
 * @see javax.swing.KeyStroke
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorToolAction {

    /**
     * Возвращает строку с названием метода
     * @return строку с названием метода
     */
    String name();

    /**
     * Возвращает строковое представления события,
     * при котором будет выполнятся метод
     * @return строковое представления события, при котором будет выполнятся метод
     */
    String hotKey();

    /**
     * Возвращает тип слушателя событий к которому будет прикреплен метод
     * @return тип слушателя событий к которому будет прикреплен метод
     */
    ListenerType listenerType() default ListenerType.KEY;
}
