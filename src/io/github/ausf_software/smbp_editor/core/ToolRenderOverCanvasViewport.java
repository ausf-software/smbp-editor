package io.github.ausf_software.smbp_editor.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация ToolRenderOverCanvasViewport используется для обозначения
 * класса как объекта для отрисовки поверх всей панели редактора. Имя,
 * которое указывается в аннотации, необходимо для последующего запроса
 * на отрисовку.
 * <p>Параметр timeRender указывает время (в миллисекундах) спустя которое
 * отрисовка данного элемента будет автоматически прекращена. Значение 0
 * указывает, что отмена отрисовки будет указана вручную.
 * <p>Параметр timeAnimation указывает время (в миллисекундах) которое будут
 * длится анимации появления и исчезания элемента. Значение 0 означает, что
 * анимация не будет производится.
 * <p>При запуске приложения, система будет искать все классы,
 * помеченные аннотацией ToolRenderOverCanvasViewport, и создавать их экземпляры.
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
    String name();
    short timeRender() default 0;
    short timeAnimation() default 0;
}
