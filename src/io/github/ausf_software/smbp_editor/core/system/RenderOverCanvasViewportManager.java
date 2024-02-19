package io.github.ausf_software.smbp_editor.core.system;

import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.ToolRenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.render.Editor;

import java.util.*;

public class RenderOverCanvasViewportManager {

    public static RenderOverCanvasViewportManager INSTANCE = new RenderOverCanvasViewportManager();

    private Map<String, RenderOverCanvasViewport> allObjects = new HashMap<>();
    private Map<String, RenderOverCanvasViewport> currentRender = new HashMap<>();

    private Timer timer = new Timer();

    private RenderOverCanvasViewportManager() {}

    /**
     * Добавляет новый объект рендера в общую базу
     * @param toolRender класс рендера
     */
    public void put(Class<?> toolRender) {
        try {
            RenderOverCanvasViewport obj = (RenderOverCanvasViewport) toolRender.newInstance();
            ToolRenderOverCanvasViewport an =
                    toolRender.getAnnotation(ToolRenderOverCanvasViewport.class);
            allObjects.put(an.name(), obj);
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
     * Получает и возвращает аннотацию для указанного объекта
     * @param o объект у которого нужно получить аннотацию ToolRenderOverCanvasViewport
     * @return аннотацию ToolRenderOverCanvasViewport от указанного объекта
     */
    private ToolRenderOverCanvasViewport getObjectInfo(RenderOverCanvasViewport o) {
        return o.getClass().getAnnotation(ToolRenderOverCanvasViewport.class);
    }
}