package org.example.digitaldrawer.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
import org.example.digitaldrawer.errors.ErrorTypes;
import org.example.digitaldrawer.panels.ErrorPanel;
import org.example.digitaldrawer.states.CanvasStates;
import org.w3c.dom.css.Rect;

import java.util.*;

/**
 * Класс, отвечающий за холст пользователя
 */
public class CanvasController extends Canvas {
    private static final double DEFAULT_BRUSH_SIZE = 6.0;
    private static final double BRUSH_SIZE_LIMIT = 100;
    private double brushSize = DEFAULT_BRUSH_SIZE;
    private final GraphicsContext gc;
    private final LinkedHashMap<PathElement, Double> drawnShapes = new LinkedHashMap<>();
    private final LinkedHashMap<Long, Rectangle> drawnInvisibleAreas = new LinkedHashMap<>();
    private long shapesCounter = 0;
    ZoomControl zoomControl;
    private double mouseAnchorX;
    private double mouseAnchorY;

    public CanvasController() {
        this(0, 0);
    }

    public CanvasController(double width, double height) {
        super(width, height);
        this.setOnScroll(zoomHandler);
        gc = this.getGraphicsContext2D();
        zoomControl = new ZoomControl();
        zoomControl.zoomProperty().addListener(o -> redrawCanvas());
        PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
            brushSize = setBrushSize(gc, Double.parseDouble(newValue));
        });
    }

    EventHandler<ScrollEvent> zoomHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            Affine affine = gc.getTransform();
            double zoom = affine.getMxx() + scrollEvent.getDeltaY() / 800;
            zoomControl.setZoom(zoom);
        }
    };

    /**
     * Регистрирует перемещения мыши пользователя над холстом
     */
    public void pressMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(CanvasStateController.getState().equals(CanvasStates.BRUSH_MODE.getStateName())){
                    if(brushSize == DEFAULT_BRUSH_SIZE){
                        PenSizeDropDownList.getPenSize().getSelectionModel().select(0);
                    }
                    double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                    double transformedX = transformedCoords[0];
                    double transformedY = transformedCoords[1];

                    gc.beginPath();
                    drawnShapes.put(new MoveTo(transformedX, transformedY), brushSize);
                    gc.moveTo(transformedX, transformedY);
                    gc.stroke();
                }
                else if(CanvasStateController.getState().equals(CanvasStates.DRAG_AND_DROP_MODE.getStateName())){
//                    ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
//            return DEFAULT_BRUSH_SIZE;

                    System.out.println("drag and drop mode");
                }
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
                if(CanvasStateController.getState().equals(CanvasStates.BRUSH_MODE.getStateName())){
                    double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                    double transformedX = transformedCoords[0];
                    double transformedY = transformedCoords[1];
                    drawnShapes.put(new LineTo(transformedX, transformedY), brushSize);
                    gc.lineTo(transformedX, transformedY);
                    gc.stroke();
                }
                else if(CanvasStateController.getState().equals(CanvasStates.DRAG_AND_DROP_MODE.getStateName())){
//                    ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
////            return DEFAULT_BRUSH_SIZE;
                    System.out.println("drag and drop mode");
                }
            }
        });
    }

    /**
     * Устанавливает размер кисти пользователя
     *
     * @param gc   - объект, необходимый для отображения нарисованных пользователем фигур
     * @param size - желаемый размер кисти
     */
    private double setBrushSize(GraphicsContext gc, double size) {
        if (size > BRUSH_SIZE_LIMIT) {
            ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
            return DEFAULT_BRUSH_SIZE;
        }
        brushSize = size;
        gc.setLineWidth(size);
        return size;
    }

    /**
     * Перерисовывает холст в соответствии с новым масштабом
     */
    private void redrawCanvas() {
        if (gc == null) {
            return;
        }

        Canvas canvas = gc.getCanvas();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setTransform(1, 0, 0, 1, 0, 0);
        gc.clearRect(0, 0, w, h);
        double z = zoomControl.getZoom();
        gc.setTransform(z, 0, 0, z, (w - w * z) / 2.0, (h - h * z) / 2.0);
        for(Map.Entry<PathElement, Double> element : this.getDrawnShapes().entrySet()){
            setBrushSize(gc, element.getValue());
            paint(gc, element);
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

    public LinkedHashMap<PathElement, Double> getDrawnShapes() {
        return drawnShapes;
    }

    /**
     * Отрисовывает холст
     *
     * @param gc
     */
    public void paint(GraphicsContext gc, Map.Entry<PathElement, Double> element){
            gc.setStroke(Color.BLACK);
                if (element.getKey() instanceof MoveTo move) {
                    gc.beginPath();
                    gc.moveTo(move.getX(), move.getY());
                } else if (element.getKey() instanceof LineTo line) {
                    gc.lineTo(line.getX(), line.getY());
                    gc.stroke();
                }
        }

    /**
     * Класс, отвечающий за зум холста
     */
    private class ZoomControl{
        private final SimpleDoubleProperty zoom = new SimpleDoubleProperty(1.0);

        /**
         * Возвращает текущее значение масштаба холста (Наблюдамое значение)
         *
         * @return масштаб холста
         */
        public DoubleProperty zoomProperty() {
            return zoom;
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


    }

    /**
     * Создание невидимого контейнера для каждой нарисованной на холсте фигуры для дальнейшего
     * взаимодействия с курсором
     * @param group - объект, который содержит в себе все контейнеры
     */
    public void createRectangleContainer(Group group){
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double minX = Double.MAX_VALUE;
                double minY = Double.MAX_VALUE;
                double maxX = Double.MIN_VALUE;
                double maxY = Double.MIN_VALUE;
                for(Map.Entry<PathElement, Double> element : drawnShapes.entrySet()){
                    if(element instanceof MoveTo move){
                        minX = Math.min(minX, move.getX());
                        minY = Math.min(minY, move.getX());
                        maxX = Math.max(maxX, move.getX());
                        maxY = Math.max(maxY, move.getY());
                        drawnInvisibleAreas.put(shapesCounter, new Rectangle(minX, minY, maxX - minX, maxY - minY));
                        drawnInvisibleAreas.get(shapesCounter).setFill(Color.TRANSPARENT);
                        drawnInvisibleAreas.get(shapesCounter).setStroke(null);
                        drawnInvisibleAreas.get(shapesCounter).toBack();
                        group.getChildren().add(drawnInvisibleAreas.get(shapesCounter));
                        makeDraggable(group, drawnInvisibleAreas.get(shapesCounter));
                        shapesCounter++;
                    }
                    else if(element instanceof LineTo line){
                        minX = Math.min(minX, line.getX());
                        minY = Math.min(minY, line.getX());
                        maxX = Math.max(maxX, line.getX());
                        maxY = Math.max(maxY, line.getY());
                    }

                }
            }
        });
    }

    /**
     * Отвечает за возможность передвижения объекта на холсте при помощи мыши
     * @param group - объект, который содержит в себе все контейнеры
     * @param rect - контейнер
     */
    private void makeDraggable(Group group, Rectangle rect){
        rect.setOnMousePressed(event ->{
            mouseAnchorX = event.getSceneX() - group.getTranslateX();
            mouseAnchorY = event.getSceneY() - group.getTranslateY();
        });

        rect.setOnMouseDragged(event ->{
            group.setTranslateX(event.getSceneX() - mouseAnchorX);
            group.setTranslateY(event.getSceneY() - mouseAnchorY);
        });
    }

}

