package io.github.ausf_software.smbp_editor.core;

import java.awt.*;

/**
 * Абстрактный класс служащий для задания методов которые должны
 * содержаться у каждого класса помеченным аннотацией ToolRenderOverCanvasViewport.
 */
public abstract class RenderOverCanvasViewport {

    private long startAnimationTime;

    /**
     * Рисует необходимое содержимое поверх всей панели редактора
     * @param g объект графики панели редактора.
     */
    public abstract void draw(Graphics g);

    /**
     * Метод который должен содержать описание изменений в
     * для анимации появления.
     * @param delta разница прошедшего времени
     */
    public abstract void animationAppearance(double delta);

    /**
     * Метод который должен содержать описание изменений в
     * для анимации исчезания.
     * @param delta разница прошедшего времени
     */
    public abstract void animationDisappearing(double delta);

    /**
     * Возвращает время начала анимации
     * @return время начала анимации (в миллисекундах)
     */
    public long getStartAnimationTime() {
        return startAnimationTime;
    }

    /**
     * Устанавливает время начала анимации
     * @param startAnimationTime время начала анимации (в миллисекундах)
     */
    public void setStartAnimationTime(long startAnimationTime) {
        this.startAnimationTime = startAnimationTime;
    }
}
