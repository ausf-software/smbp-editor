package io.github.ausf_software.smbp_editor.window.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.Map;

public class Editor extends JComponent {

    public static Editor INSTANCE = new Editor();

    private EditorPanelEvent editorEvent;

    /**
     * Количество пикселей которое будет отрисовываться
     * для линии длиной в один метр, при 100% масштабе
     */
    public static final byte PIXELS_IN_METER = 50;

    // --- События которые нельзя поместить в InputMap -----------------

    private final int MOUSE_WHEEL_DOWN = 1;
    private final int MOUSE_WHEEL_UP = -1;
    private String mouseWheelDownAction = null;
    private String mouseWheelUpAction = null;

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
    private final byte sizeLiner = 20; // толщина линейки по краю редактора
    private final byte linerSplitter = 50; // кол-во пикселей между метками линейки редактора
    private final short maxViewOverCanvas = 250; // кол-во пикселей пространства за холстом

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

    public void registerInputMap(Map<KeyStroke, Object> inputMap) {
        for (Map.Entry<KeyStroke, Object> entry : inputMap.entrySet()) {
            getInputMap(JComponent.WHEN_FOCUSED).put(entry.getKey(), entry.getValue());
        }
    }

    public void registerActionMap(Map<Object, AbstractAction> inputMap) {
        for (Map.Entry<Object, AbstractAction> entry : inputMap.entrySet()) {
            getActionMap().put(entry.getKey(), entry.getValue());
        }
    }

    public void registerMouseWheelUpActionId(String action) {
        mouseWheelUpAction = action;
    }

    public void registerMouseWheelDownActionId(String action) {
        mouseWheelDownAction = action;
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
        int endW = getWidth() - sizeLiner;
        endW -= endW - sizeCanvasWidth;
        return endW;
    }

    private int getEndHeightLiner() {
        int endH = getHeight() - sizeLiner;
        endH -= endH - sizeCanvasWidth;
        return endH;
    }

    private void drawLiner(Graphics g) {
        g.setColor(linerColor);
        g.fillRect(0, 0, getWidth(), sizeLiner);
        g.fillRect(0, 0, sizeLiner, getHeight());

        int startW = sizeLiner + currCanvasX;
        int startH = sizeLiner + currCanvasY;
        int endW = getEndWidthLiner();
        int endH = getEndHeightLiner();

        int drs = (int) (sizeLiner / 1.5);
        int sps = linerSplitter / 5;
        int lnс = sizeLiner - 1; // длина линии = sizeLiner => последняя координата sizeLiner - 1

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
        g.fillRect(0, 0, sizeLiner, sizeLiner);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.translate(currCanvasX + sizeLiner, currCanvasY + sizeLiner);
        drawCanvas(g);
        g.translate(-currCanvasX - sizeLiner, -currCanvasY - sizeLiner);
        drawLiner(g);
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
     * Перемещает холст на указанное значение смещения
     * @param x величина смещения по оси X
     * @param y величина смещения по оси Y
     */
    public void moveCanvasCords(int x, int y) {
        currCanvasX += x;
        currCanvasY += y;
        repaint();
    }

    /**
     * Увеличивает масштаб холста на указанное значение
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
     * Возвращает точку с текущим смещением позиции холста
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
            String name = r == MOUSE_WHEEL_UP ? mouseWheelUpAction : mouseWheelDownAction;

            if (name != null) {
                Action action = getActionMap().get(name);
                if (action != null)
                    action.actionPerformed(null);
            }
        }
    }

}