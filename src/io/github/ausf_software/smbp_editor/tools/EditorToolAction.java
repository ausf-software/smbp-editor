package io.github.ausf_software.smbp_editor.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see io.github.ausf_software.smbp_editor.input.MouseWheelStroke
 * @see javax.swing.KeyStroke
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EditorToolAction {
    String name();
    String hotKey();
    ListenerType listenerType() default ListenerType.KEY;

    enum ListenerType {
        KEY,
        MOUSE_WHEEL;
    }
}
