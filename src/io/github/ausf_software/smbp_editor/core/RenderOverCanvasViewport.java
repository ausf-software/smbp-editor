package io.github.ausf_software.smbp_editor.core;

import java.awt.*;

/**
 * Абстрактный класс служащий для задания методов которые должны
 * содержаться у каждого класса помеченным аннотацией <code>ToolRenderOverCanvasViewport</code>.
 */
public abstract class RenderOverCanvasViewport {

    /**
     * Рисует необходимое содержимое поверх всей панели редактора
     * @param g объект графики панели редактора.
     */
    public abstract void draw(Graphics g);

}
