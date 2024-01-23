package io.github.ausf_software.smbp_editor.input;

import java.awt.event.InputEvent;

/**
 * <code>MouseWheelStroke</code> представляет собой вращение колесика мыши. Во всех случаях
 * <code>MouseWheelStroke</code> может указывать модификаторы (alt, Shift, Control или их
 * комбинацию), которые должны присутствовать во время действия для точного совпадения.
 * <p>Объекты <code>MouseWheelStroke</code> неизменяемы и должны быть уникальными. Создать напрямую
 * экземпляр <code>MouseWheelStroke</code> нельзя. Вместо этого используются фабричные методы
 * <code>getMouseWheelStroke</code>.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
public class MouseWheelStroke {

    /**
     * Код события прокрутки колесика мыши вниз
     */
    public static final int MOUSE_WHEEL_DOWN = 1;
    /**
     * Код события прокрутки колесика мыши вверх
     */
    public static final int MOUSE_WHEEL_UP = 2;

    private int mask;

    private MouseWheelStroke(int mask) {
        this.mask = mask;
    }

    /**
     * Создает и возвращает <code>MouseWheelStroke</code> по указанным параметрам.
     * @param shift зажат ли shift
     * @param alt зажат ли alt
     * @param ctrl зажат ли ctrl
     * @param wheelInput код типа события, определенные в константах MOUSE_WHEEL_DOWN и
     *                   MOUSE_WHEEL_UP
     * @return объект MouseWheelStroke для указанных параметров или значение NULL, если указанный
     * параметр типа события колесика мыши не равен упомянутым константам
     */
    public static MouseWheelStroke getMouseWheelStroke(boolean shift, boolean alt, boolean ctrl,
                                                       int wheelInput) {
        int temp = 0;
        if (shift)
            temp += InputEvent.SHIFT_DOWN_MASK;
        if (alt)
            temp += InputEvent.ALT_DOWN_MASK;
        if (ctrl)
            temp += InputEvent.CTRL_DOWN_MASK;
        if (wheelInput == MOUSE_WHEEL_UP || wheelInput == MOUSE_WHEEL_DOWN) {
            temp += wheelInput;
            return new MouseWheelStroke(temp);
        }
        return null;
    }

    /**
     * Анализирует строку и возвращает <code>MouseWheelStroke</code>. Строка должна иметь
     * следующий синтаксис:
     * <pre>
     *    &lt;downButton&gt;* (&lt;mouseWheelUp&gt; | &lt;mouseWheelDown&gt;)
     *
     *    downButton := shift | SHIFT | ctrl | CTRL | alt | ALT
     *    mouseWheelUp := mouseWheelUp | MOUSE_WHEEL_UP
     *    mouseWheelDown := mouseWheelDown | MOUSE_WHEEL_DOWN
     * </pre>
     * @param key строка, отформатированная, как описано выше
     * @return объект MouseWheelStroke для этой строки или значение NULL, если указанная строка
     * имеет значение NULL или имеет неправильный формат.
     */
    public static MouseWheelStroke getMouseWheelStroke(String key) {
        int temp = 0;
        if (key.contains("ctrl") || key.contains("CTRL")
            || key.contains("ctr") || key.contains("CTR"))
            temp += InputEvent.CTRL_DOWN_MASK;
        if (key.contains("alt") || key.contains("ALT"))
            temp += InputEvent.ALT_DOWN_MASK;
        if (key.contains("shift") || key.contains("SHIFT"))
            temp += InputEvent.SHIFT_DOWN_MASK;
        if ((key.contains("mouseWheelUp") || key.contains("MOUSE_WHEEL_UP"))
                && !(key.contains("mouseWheelDown") || key.contains("MOUSE_WHEEL_DOWN"))) {
            temp += MOUSE_WHEEL_UP;
            return new MouseWheelStroke(temp);
        }
        if ((key.contains("mouseWheelDown") || key.contains("MOUSE_WHEEL_DOWN"))
                && !(key.contains("mouseWheelUp") || key.contains("MOUSE_WHEEL_UP"))) {
            temp += MOUSE_WHEEL_UP;
            return new MouseWheelStroke(temp);
        }
        return null;
    }

}
