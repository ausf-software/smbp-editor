package io.github.ausf_software.smbp_editor.core.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация <code>EditorTool</code> используется для обозначения класса как
 * инструмента редактора. Имя, которое указывается в аннотации,
 * необходимо для отображения в окне настройки горячих клавиш.
 * <p>При запуске приложения, система будет искать все классы,
 * помеченные аннотацией <code>EditorTool</code>, и создавать их экземпляры.
 * Это позволяет динамически добавлять новые инструменты редактора
 * без необходимости изменения основного кода приложения.
 * <p>По умолчанию инструмент не добавляется на панель инструментов, для
 * этого необходимо указать путь к иконке инструмента. При указании
 * система автоматически добавит инструмент на панель инструментов.
 * Важно: путь к файлу указывается начиная с указания папки ресурсов.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorTool {
    /**
     * Возвращает строку с названием инструмента
     * @return строку с названием инструмента
     */
    String name();

    /**
     * Возвращает строку с путем к иконке инструмента.
     * <p>В случае если инструмент не добавляется на панель
     * инструментов, то вернется по умолчанию пустая строка.
     * @return строку с путем к иконке инструмента
     */
    String icon() default "";

    /**
     * Возвращает строку с именем конфигурационного файла
     * @return строку с именем конфигурационного файла
     */
    String cfg() default "";
}
