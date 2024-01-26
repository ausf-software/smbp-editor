package io.github.ausf_software.smbp_editor.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация EditorTool используется для обозначения класса как
 * инструмента редактора. Имя, которое указывается в аннотации,
 * необходимо для отображения в окне настройки горячих клавиш.
 * <p>При запуске приложения, система будет искать все классы,
 * помеченные аннотацией EditorTool, и создавать их экземпляры.
 * Это позволяет динамически добавлять новые инструменты редактора
 * без необходимости изменения основного кода приложения.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorTool {
    String name();
}
