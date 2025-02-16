package org.example.digitaldrawer.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
import org.example.digitaldrawer.errors.ErrorTypes;
import org.example.digitaldrawer.panels.ErrorPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за холст пользователя
 */
public abstract class CanvasController extends Canvas {
    private double brushSize = 5.0;
    private GraphicsContext gc;
    private SimpleDoubleProperty zoom = new SimpleDoubleProperty(1.0);
    private List<PathElement> drawnShapes = new ArrayList<>();

    public CanvasController() {
        this(0, 0);
    }

    public CanvasController(double width, double height) {
        super(width, height);
        this.setOnScroll(zoomHandler);
        this.zoomProperty().addListener(o -> redrawCanvas());
        gc = this.getGraphicsContext2D();
    }

    EventHandler<ScrollEvent> zoomHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            CanvasController zoomedCanvas = (CanvasController) scrollEvent.getSource();
            GraphicsContext gc = zoomedCanvas.getGraphicsContext2D();
            Affine affine = gc.getTransform();
            double zoom = affine.getMxx() + scrollEvent.getDeltaY() / 800;
            zoomedCanvas.setZoom(zoom);
            zoomedCanvas.redrawCanvas();
        }
    };

    /**
     * Регистрирует перемещения мыши пользователя над холстом
     */
    public void pressMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
                    brushSize = Double.parseDouble(newValue);
                });
                double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                double transformedX = transformedCoords[0];
                double transformedY = transformedCoords[1];
                setBrushSize(gc, brushSize);
                gc.beginPath();
                drawnShapes.add(new MoveTo(transformedX, transformedY));
                gc.moveTo(transformedX, transformedY);
                gc.stroke();
            }
        });
    }

    /**
     * Отрисовывает линии кистью пользователя
     */
    public void dragMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                double transformedX = transformedCoords[0];
                double transformedY = transformedCoords[1];
                drawnShapes.add(new LineTo(transformedX, transformedY));
                gc.lineTo(transformedX, transformedY);
                gc.stroke();
            }
        });
    }

    /**
     * Устанавливает размер кисти пользователя
     *
     * @param gc   - объект, необходимый для отображения нарисованных пользователем фигур
     * @param size - желаемый размер кисти
     */
    public void setBrushSize(GraphicsContext gc, double size) {
        if (size > 100) {
            ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
        }
        gc.setLineWidth(size);
    }

    /**
     * Возвращает текущее значение масштаба холста (Наблюдамое значение)
     *
     * @return масштаб холста
     */
    public DoubleProperty zoomProperty() {
        return zoom;
    }

    /**
     * Перерисовывает холст в соответствии с новым масштабом
     */
    public void redrawCanvas() {
        if (gc == null) {
            return;
        }

        Canvas canvas = gc.getCanvas();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setTransform(1, 0, 0, 1, 0, 0);
        gc.clearRect(0, 0, w, h);
        double z = getZoom();
        gc.setTransform(z, 0, 0, z, (w - w * z) / 2.0, (h - h * z) / 2.0);
        setBrushSize(gc, brushSize);
        paint(gc);
    }

    public double getZoom() {
        return zoom.get();
    }

    public void setZoom(double value) {
        if (value != getZoom()) {
            this.zoom.set(value);
            redrawCanvas();
        }
    }

    /**
     * Трансформация координат для корректного масштабирования курсора пользователя
     *
     * @param x - координата х
     * @param y - координата у
     * @return пару координат x, y для курсора
     */
    private double[] transformCoordinates(double x, double y) {
        try {
            Affine inverseTransform = gc.getTransform().createInverse();
            double[] coords = new double[]{x, y};
            inverseTransform.transform2DPoints(coords, 0, coords, 0, 1);
            return coords;
        } catch (NonInvertibleTransformException e) {
            System.err.println("Impossible to convert coordinates!");
            return new double[]{x, y};
        }
    }

    public List<PathElement> getDrawnShapes() {
        return drawnShapes;
    }

    /**
     * Отрисовывает холст
     *
     * @param gc
     */
    public abstract void paint(GraphicsContext gc);
}
