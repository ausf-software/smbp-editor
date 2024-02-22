package io.github.ausf_software.smbp_editor.render;

import io.github.ausf_software.smbp_editor.core.RenderOverCanvasViewport;
import io.github.ausf_software.smbp_editor.core.system.RenderOverCanvasViewportManager;
import io.github.ausf_software.smbp_editor.input.ActionInputMap;
import io.github.ausf_software.smbp_editor.input.MouseWheelStroke;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import static io.github.ausf_software.smbp_editor.input.MouseWheelStroke.*;

public class Editor extends JComponent {

    public static Editor INSTANCE = new Editor();

    private EditorPanelEvent editorEvent;

    /**
     * Количество пикселей которое будет отрисовываться
     * для линии длиной в один метр, при 100% масштабе
     */
    public static final byte PIXELS_IN_METER = 50;

    // --- События которые нельзя поместить в InputMap -----------------

    private final int MOUSE_WHEEL_DOWN_VALUE = 1;
    private final int MOUSE_WHEEL_UP_VALUE = -1;
    private HashMap<MouseWheelStroke, String> mouseWheelMap = null;
    private HashMap<MouseWheelStroke, String> renderOverCanvasViewportMap = null;

    // --- Текущее смещение холста относительно нуля -------------------

    private int currCanvasX = 50;
    private int currCanvasY = 50;

    // --- Размер холста -----------------------------------------------

    // TODO: ограничить пользователю выход за пределы int
    private int sizeCanvasWidth = 500;
    private int sizeCanvasHeight = 500;

    private int splitterSize = 1; // Размер сетки
    private int scale = 100; // Текущий масштаб в процентах

    // --- Параметры отображения и перемещения холста ------------------

    private final byte canvasDepth = 2; // отступ создания объема рабочего полотна
    /**
     * Толщина линейки по краю редактора
     */
    public final byte SIZE_LINER = 20;
    private final byte linerSplitter = 50; // кол-во пикселей между метками линейки редактора
    /**
     * Кол-во пикселей пространства за холстом
     */
    public final short MAX_VIEW_OVER_CANVAS = 250;

    // --- Цвета темы --------------------------------------------------

    private Color linerColor = new Color(40, 40, 40); // фон линейки редактора

    // -----------------------------------------------------------------

    private Editor() {
        setFocusable(true);
        editorEvent = new EditorPanelEvent(this);
        addMouseListener(editorEvent);
        addFocusListener(editorEvent);
        addMouseWheelListener(editorEvent);
    }

    // --- Регистрация событий -----------------------------------------

    public void registerActionInputMap(ActionInputMap actionInputMap) {
        registerActionMap(actionInputMap.getActionMap());
        registerInputMap(actionInputMap.getKeyMap());
        registerMouseWheelMap(actionInputMap.getMouseWheelMap());
    }

    private void registerInputMap(Map<KeyStroke, Object> inputMap) {
        for (Map.Entry<KeyStroke, Object> entry : inputMap.entrySet()) {
            getInputMap(JComponent.WHEN_FOCUSED).put(entry.getKey(), entry.getValue());
        }
    }

    private void registerActionMap(Map<Object, AbstractAction> inputMap) {
        for (Map.Entry<Object, AbstractAction> entry : inputMap.entrySet()) {
            getActionMap().put(entry.getKey(), entry.getValue());
        }
    }

    private void registerMouseWheelMap(HashMap<MouseWheelStroke, String> mouseWheelMap) {
        this.mouseWheelMap = mouseWheelMap;
    }

    // --- Отрисовка ---------------------------------------------------

    private void drawCanvas(Graphics g) {
        double s = (double) (scale) / 100;
        int scaledWidth = (int) (s * sizeCanvasWidth);
        int scaledHeight = (int) (s * sizeCanvasHeight);

        // подложка холста
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, scaledWidth, scaledHeight);

        // контур холста
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, scaledWidth, scaledHeight);

        // правая сторона объема
        g.drawLine(canvasDepth + scaledWidth, canvasDepth,
                canvasDepth + scaledWidth,
                canvasDepth + scaledHeight);
        // нижняя сторона объема
        g.drawLine(canvasDepth, canvasDepth + scaledHeight,
                canvasDepth + scaledWidth,
                canvasDepth + scaledHeight);

        // нижняя левая соединяющая полоса
        g.drawLine(0, scaledHeight, canvasDepth,
                canvasDepth + scaledHeight);

        // нижняя правая соединяющая полоса
        g.drawLine(scaledWidth, scaledHeight,
                canvasDepth + scaledWidth,
                canvasDepth + scaledHeight);

        // верхняя правая соединяющая полоса
        g.drawLine(scaledWidth, 0,
                canvasDepth + scaledWidth,
                canvasDepth);
    }

    private int getEndWidthLiner() {
        int endW = getWidth() - SIZE_LINER;
        endW -= endW - sizeCanvasWidth;
        return endW;
    }

    private int getEndHeightLiner() {
        int endH = getHeight() - SIZE_LINER;
        endH -= endH - sizeCanvasWidth;
        return endH;
    }

    private void drawLiner(Graphics g) {
        g.setColor(linerColor);
        g.fillRect(0, 0, getWidth(), SIZE_LINER);
        g.fillRect(0, 0, SIZE_LINER, getHeight());

        int startW = SIZE_LINER + currCanvasX;
        int startH = SIZE_LINER + currCanvasY;
        int endW = getEndWidthLiner();
        int endH = getEndHeightLiner();

        int drs = (int) (SIZE_LINER / 1.5);
        int sps = linerSplitter / 5;
        int lnс = SIZE_LINER - 1; // длина линии = sizeLiner => последняя координата sizeLiner - 1

        g.setColor(Color.WHITE);
        for (int i = 0; i < endW / linerSplitter + 1; i++) {
            int num = i * linerSplitter + startW;
            g.drawLine(num, 0, num, lnс);
            for (int k = 1; k < 5; k++) {
                int subLineX = num + k * sps;
                g.drawLine(subLineX, lnс, subLineX, drs);
            }
        }

        for (int i = 0; i < endH / linerSplitter + 1; i++) {
            int num = i * linerSplitter + startH;
            g.drawLine(0, num, lnс, num);
            for (int k = 1; k < 5; k++) {
                int subLineY = num + k * sps;
                g.drawLine(lnс, subLineY, drs, subLineY);
            }
        }

        g.setColor(linerColor);
        g.fillRect(0, 0, SIZE_LINER, SIZE_LINER);
    }

    private void drawToolContentOnCanvas(Graphics g) {

    }

    private void drawToolContentOverCanvasViewport(Graphics g) {
        for (RenderOverCanvasViewport r : RenderOverCanvasViewportManager.INSTANCE.getRenderList())
            r.draw(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = currCanvasX + SIZE_LINER;
        int y = currCanvasY + SIZE_LINER;
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.translate(x, y);
        drawCanvas(g);

        drawToolContentOnCanvas(g);

        g.translate(-x, -y);
        drawLiner(g);
        drawToolContentOverCanvasViewport(g);
    }

    // -----------------------------------------------------------------

    /**
     * Устанавливает новый размер холста. После изменения выполнится перерисовка
     * @param widthMeters ширина в метрах
     * @param heightMeters высота в метрах
     */
    public void setCanvasSize(int widthMeters, int heightMeters) {
        sizeCanvasHeight = heightMeters * PIXELS_IN_METER;
        sizeCanvasWidth = widthMeters * PIXELS_IN_METER;
        repaint();
    }

    /**
     * Возвращает ширину холста
     * @return ширину холста
     */
    public int getCanvasWidth() {
        return sizeCanvasWidth;
    }

    /**
     * Возвращает высоту холста
     * @return высоту холста
     */
    public int getCanvasHeight() {
        return sizeCanvasHeight;
    }

    /**
     * Перемещает холст на указанное значение смещения. После смещения выполнится перерисовка
     * @param x величина смещения по оси X
     * @param y величина смещения по оси Y
     */
    public void moveCanvasCords(int x, int y) {
        currCanvasX += x;
        currCanvasY += y;
        repaint();
    }

    /**
     * Увеличивает масштаб холста на указанное значение. После увеличения выполнится перерисовка
     * @param value величина приближения в процентах
     */
    public void addZoom(int value) {
        scale += value;
        repaint();
    }

    /**
     * Возвращает текущий масштаб холста в процентах
     * @return текущий масштаб холста в процентах.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Возвращает новую точку с текущим смещением позиции холста.
     * <p>При использовании в блоке кода несколько раз этой точки
     * рекомендуется записать ссылку на возвращаемую точку
     * в отдельную переменную, а не вызывать многократно метод.
     * @return точку с текущим смещением позиции холста.
     */
    public Point getCurrentCanvasPosition() {
        return new Point(currCanvasX, currCanvasY);
    }

    /**
     * Возвращает текущее смещение позиции холста по оси X
     * @return текущее смещение позиции холста по оси X.
     */
    public int getCurrentCanvasX() {
        return currCanvasX;
    }

    /**
     * Возвращает текущее смещение позиции холста по оси Y
     * @return текущее смещение позиции холста по оси Y.
     */
    public int getCurrentCanvasY() {
        return currCanvasY;
    }

    private class EditorPanelEvent extends EventObject
            implements MouseListener, FocusListener, MouseWheelListener {

        private boolean isFocused = false;

        public EditorPanelEvent(Editor source) {
            super(source);
        }

        // --- FocusListener methods -----------------------------------

        @Override
        public void focusGained(FocusEvent e) {
            isFocused = true;
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {
            isFocused = false;
            repaint();
        }

        // --- MouseListener methods -----------------------------------

        @Override
        public void mouseClicked(MouseEvent e) {
            requestFocusInWindow();
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        // --- MouseWheelListener methods ------------------------------

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int r = e.getWheelRotation();
            int wheelType = r == MOUSE_WHEEL_UP_VALUE ? MOUSE_WHEEL_UP : MOUSE_WHEEL_DOWN;
            MouseWheelStroke stroke = getMouseWheelStroke(e.isShiftDown(), e.isAltDown(),
                    e.isControlDown(), wheelType);
            if (mouseWheelMap != null) {
                Action action = getActionMap().get(mouseWheelMap.get(stroke));
                if (action != null)
                    action.actionPerformed(null);
            }
        }
    }

}
