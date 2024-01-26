package io.github.ausf_software.smbp_editor.core;

import io.github.ausf_software.smbp_editor.render.Editor;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RenderOverCanvasViewportManager {

    public static RenderOverCanvasViewportManager INSTANCE = new RenderOverCanvasViewportManager();

    private Map<String, RenderOverCanvasViewport> allObjects = new HashMap<>();
    private Map<String, RenderOverCanvasViewport> currentRender = new HashMap<>();

    private Queue<RenderOverCanvasViewport> appearanceQueue = new ConcurrentLinkedQueue<>();
    private Queue<RenderOverCanvasViewport> disappearingQueue = new ConcurrentLinkedQueue<>();

    private Timer timer = new Timer();
    private Thread animationThread = new AnimationThread();
    private boolean animated = false;
    private final double ANIMATION_FRAMES = 1000000000.0 / 120.0;

    private RenderOverCanvasViewportManager() {}

    /**
     * Добавляет новый объект рендера в общую базу
     * @param toolRender класс рендера
     */
    protected void put(Class<?> toolRender) {
        try {
            System.out.println(toolRender);
            allObjects.put(toolRender.getAnnotation(ToolRenderOverCanvasViewport.class).name(),
                    (RenderOverCanvasViewport) toolRender.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет в список рендера объектов, объект с указанным именем
     * @param name имя объекта рендера поверх всей панели редактора,
     *             которое необходимо отрисовать
     */
    public void requestRender(String name) {
        RenderOverCanvasViewport o = allObjects.get(name);
        currentRender.put(name, o);
        ToolRenderOverCanvasViewport a = getObjectInfo(o);
        if (a.timeAnimation() != 0) {
            appearanceQueue.add(o);
            o.setStartAnimationTime(System.nanoTime());
            if (!animated)
                animationThread.run();
        }
        if (a.timeRender() != 0) {
            TimerTask autoReject = new TimerTask() {
                @Override
                public void run() {
                    rejectRender(name);
                }
            };
            timer.schedule(autoReject, a.timeRender());
        }
        Editor.INSTANCE.repaint();
    }

    /**
     * Отменяет отрисовку указанного объекта рендера
     * @param name имя объекта рендера поверх всей панели редактора,
     *             которое необходимо прекратить отрисовать
     */
    public void rejectRender(String name) {
        currentRender.remove(name);
        Editor.INSTANCE.repaint();
    }

    /**
     * Получить коллекцию объектов которые сейчас отрисовываются
     * @return коллекцию объектов которые сейчас отрисовываются
     */
    public Collection<RenderOverCanvasViewport> getRenderList() {
        return currentRender.values();
    }

    /**
     * Проверка на не пустоту хотябы одной очереди на анимацию
     * @return true если хотя бы в одной очереди на анимацию есть
     * по крайней мере один элемент
     */
    private boolean animationIsNotEmpty() {
        return !appearanceQueue.isEmpty() || !disappearingQueue.isEmpty();
    }

    /**
     * Получает и возвращает аннотацию для указанного объекта
     * @param o объект у которого нужно получить аннотацию ToolRenderOverCanvasViewport
     * @return аннотацию ToolRenderOverCanvasViewport от указанного объекта
     */
    private ToolRenderOverCanvasViewport getObjectInfo(RenderOverCanvasViewport o) {
        return o.getClass().getAnnotation(ToolRenderOverCanvasViewport.class);
    }

    private class AnimationThread extends Thread {
        @Override
        public void run() {
            animated = true;
            long lastTime = System.nanoTime();

            while (animationIsNotEmpty()) {
                long now = System.nanoTime();
                double delta = (now - lastTime) / ANIMATION_FRAMES;
                lastTime = now;

                while (!appearanceQueue.isEmpty()) {
                    RenderOverCanvasViewport animation = appearanceQueue.poll();
                    ToolRenderOverCanvasViewport info = getObjectInfo(animation);
                    /*if (lastTime - animation.getStartAnimationTime() >= info.timeAnimation())
                        appearanceQueue.remove();*/
                    animation.animationAppearance(delta);
                }

                while (!disappearingQueue.isEmpty()) {
                    RenderOverCanvasViewport animation = disappearingQueue.poll();
                    ToolRenderOverCanvasViewport info = getObjectInfo(animation);
                    /*if (lastTime - animation.getStartAnimationTime() >= info.timeAnimation())
                        disappearingQueue.remove();*/
                    animation.animationDisappearing(delta);
                }

                Editor.INSTANCE.repaint();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            animated = false;
        }
    }
}