package io.github.ausf_software.smbp_editor.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация <code>ToolRenderOverCanvasViewport</code> используется для обозначения
 * класса как объекта для отрисовки поверх всей панели редактора. Имя,
 * которое указывается в аннотации, необходимо для последующего запроса
 * на отрисовку.
 * <p>Параметр <code>timeRender</code> указывает время (в миллисекундах) спустя которое
 * отрисовка данного элемента будет автоматически прекращена. Значение 0
 * указывает, что отмена отрисовки будет указана вручную.
 * <p>При запуске приложения, система будет искать все классы,
 * помеченные аннотацией <code>ToolRenderOverCanvasViewport</code>, и создавать их экземпляры.
 * Это позволяет динамически добавлять все дополнительные элементы отрисовки
 * поверх редактора без необходимости изменения основного кода приложения.
 * @see RenderOverCanvasViewport
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolRenderOverCanvasViewport {
    /**
     * Возвращает строку с именем элемента рендера
     * @return строку с именем элемента рендера
     */
    String name();

    /**
     * Возвращает время (в миллисекундах) спустя которое
     * отрисовка данного элемента будет автоматически прекращена
     * @return время (в миллисекундах) спустя которое
     * отрисовка данного элемента будет автоматически прекращена
     */
    short timeRender() default 0;
}
