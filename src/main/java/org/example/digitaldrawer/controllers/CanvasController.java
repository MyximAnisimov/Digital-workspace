package org.example.digitaldrawer.controllers;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
import org.example.digitaldrawer.states.CanvasStates;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за холст пользователя
 */
public class CanvasController extends Canvas {
    private static final double DEFAULT_BRUSH_SIZE = 6.0;
    private double brushSize = DEFAULT_BRUSH_SIZE;
    private final GraphicsContext gc;

    private final List<StrokeShape> strokes = new ArrayList<>();

    private StrokeShape currentStroke = null;

    private StrokeShape selectedStroke = null;

    private double dragOffsetX = 0;
    private double dragOffsetY = 0;
    CanvasRedrawer canvasRedrawer;

    public CanvasController() {
        this(0, 0);
    }

    public CanvasController(double width, double height) {
        super(width, height);
        gc = this.getGraphicsContext2D();

        canvasRedrawer = new CanvasRedrawer(strokes, gc);
        this.setOnScroll(canvasRedrawer.getZoomHandler());

        PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
            brushSize = BrushSizeController.setBrushSize(gc, Double.parseDouble(newValue));
        });

        pressMouseResponse();
        dragMouseResponse();
        releaseMouseResponse();
    }

    /**
     * При нажатии мыши выбираем логику в зависимости от состояния
     */
    public void pressMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (CanvasStateController.getState().equals(CanvasStates.BRUSH_MODE.getStateName())) {
                if (brushSize == DEFAULT_BRUSH_SIZE) {
                    PenSizeDropDownList.getPenSize().getSelectionModel().select(0);
                }

                double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                double transformedX = transformedCoords[0];
                double transformedY = transformedCoords[1];

                currentStroke = new StrokeShape(brushSize);
                currentStroke.addElement(new MoveTo(transformedX, transformedY));

                gc.beginPath();
                gc.moveTo(transformedX, transformedY);
                gc.stroke();

            } else if (CanvasStateController.getState().equals(CanvasStates.DRAG_AND_DROP_MODE.getStateName())) {
                double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                double x = transformedCoords[0];
                double y = transformedCoords[1];
                selectedStroke = null;
                for (int i = strokes.size() - 1; i >= 0; i--) {
                    StrokeShape stroke = strokes.get(i);
                    if (stroke.contains(x, y)) {
                        selectedStroke = stroke;
                        dragOffsetX = x - stroke.getOffsetX();
                        dragOffsetY = y - stroke.getOffsetY();
                        break;
                    }
                }
            }
        });
    }

    /**
     * При перетаскивании мыши дорисовываем (в режиме кисти) либо перетаскиваем (в режиме Drag&Drop).
     */
    public void dragMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (CanvasStateController.getState().equals(CanvasStates.BRUSH_MODE.getStateName())) {
                if (currentStroke != null) {
                    double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                    double transformedX = transformedCoords[0];
                    double transformedY = transformedCoords[1];

                    currentStroke.addElement(new LineTo(transformedX, transformedY));

                    gc.lineTo(transformedX, transformedY);
                    gc.stroke();
                }

            } else if (CanvasStateController.getState().equals(CanvasStates.DRAG_AND_DROP_MODE.getStateName())) {
                if (selectedStroke != null) {
                    double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
                    double x = transformedCoords[0];
                    double y = transformedCoords[1];

                    double newOffsetX = x - dragOffsetX;
                    double newOffsetY = y - dragOffsetY;

                    selectedStroke.setOffsetX(newOffsetX);
                    selectedStroke.setOffsetY(newOffsetY);
                    canvasRedrawer.redrawCanvas();
                }
            }
        });
    }

    /**
     * При отпускании мыши:
     *  1) В режиме кисти — завершаем штрих и добавляем его в общий список strokes.
     *  2) В режиме drag&drop — «отпускаем» выбранный объект.
     */
    private void releaseMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (CanvasStateController.getState().equals(CanvasStates.BRUSH_MODE.getStateName())) {
                if (currentStroke != null) {
                    // Добавляем штрих в общий список
                    strokes.add(currentStroke);
                    currentStroke = null;
                }
            } else if (CanvasStateController.getState().equals(CanvasStates.DRAG_AND_DROP_MODE.getStateName())) {
                selectedStroke = null;
            }
        });
    }

    /**
     * Трансформация координат для корректного масштабирования курсора пользователя
     *
     * @param x - координата x
     * @param y - координата y
     * @return массив [x, y], преобразованные к «логическим» координатам
     */
    private double[] transformCoordinates(double x, double y) {
        try {
            Affine inverseTransform = gc.getTransform().createInverse();
            double[] coords = new double[] { x, y };
            inverseTransform.transform2DPoints(coords, 0, coords, 0, 1);
            return coords;
        } catch (NonInvertibleTransformException e) {
            System.err.println("Impossible to convert coordinates!");
            return new double[] { x, y };
        }
    }

}
