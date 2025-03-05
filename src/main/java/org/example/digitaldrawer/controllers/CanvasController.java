package org.example.digitaldrawer.controllers;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
import org.example.digitaldrawer.controllers.functionalinterfaces.*;
import org.example.digitaldrawer.controllers.handlers.BrushController;
import org.example.digitaldrawer.controllers.handlers.DnDController;
import org.example.digitaldrawer.controllers.handlers.TextController;
import org.example.digitaldrawer.shapes.StrokeShape;
import org.example.digitaldrawer.states.CanvasStates;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Класс, отвечающий за холст пользователя
 */
public class CanvasController extends Canvas {
    private final GraphicsContext gc;
    private final Group root;
    private final List<StrokeShape> strokes = new ArrayList<>();
    private BrushController brushController = null;
    private DnDController dnDController = null;
    private TextController textController = null;
    private CanvasRedrawer canvasRedrawer;
    private HashMap<String, HashMap<String, Operations>> operations = new HashMap<>();
    private HashMap<String, Operations> operationsForTextController = new HashMap<>();
    private HashMap<String, Operations> operationsForBrushController = new HashMap<>();
    private HashMap<String, Operations> operationsForDnDController = new HashMap<>();
    BiConsumer<Double, Double> mousePressedForTextController;
    QuadroFI<Double, Group, CanvasController> mouseReleasedForText;
    TrioFI<Double, GraphicsContext> mouseDraggedForBrush;
    TrioFI<Double, GraphicsContext> mousePressedForBrush;
    TrioFI<Double, List<StrokeShape>> mousePresssedForDnD;
    TrioFI<Double, CanvasRedrawer> mouseDraggedForDnd;
    Consumer<List<StrokeShape>> mouseReleasedForBrush;
    Runnable mouseReleasedForDnd;


    public CanvasController() {
        this(0, 0, null);
    }

    public CanvasController(double width, double height, Group root) {
        super(width, height);
        gc = this.getGraphicsContext2D();
        this.root = root;
        canvasRedrawer = new CanvasRedrawer(strokes, gc);
        setOnScroll(canvasRedrawer.getZoomHandler());
        brushController = new BrushController();
        dnDController = new DnDController();
        textController = new TextController();
        PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
            BrushController.setBrushSize(gc, Double.parseDouble(newValue));
        });
        mousePressedForTextController = textController::mousePressed;
        mouseReleasedForText = textController::mouseReleased;
        mouseDraggedForBrush = brushController::mouseDragged;
        mousePressedForBrush = brushController::mousePressed;
        mouseReleasedForBrush = brushController::mouseReleased;
        mousePresssedForDnD = dnDController::mousePressed;
        mouseReleasedForDnd = dnDController::mouseReleased;
        mouseDraggedForDnd = dnDController::mouseDragged;
        operationsForTextController.putAll(Map.of("textP", new BiArgsOperation<>(mousePressedForTextController), "textR", new QuadroArgsOperation<>(mouseReleasedForText)));
        operationsForBrushController.putAll(Map.of("brushP", new TrioArgsOperation<>(mousePressedForBrush), "brushD", new TrioArgsOperation<>(mouseDraggedForBrush),"brushR", new UnaryArgsOperation<>(mouseReleasedForBrush)));
        operationsForDnDController.putAll(Map.of("dndP", new TrioArgsOperation<>(mousePresssedForDnD), "dndR", new ZeroArgsOperation(mouseReleasedForDnd), "dndD", new TrioArgsOperation<>(mouseDraggedForDnd)));
        operations.putAll(Map.of(CanvasStates.TEXT_MODE.getStateName(), operationsForTextController, CanvasStates.BRUSH_MODE.getStateName(), operationsForBrushController, CanvasStates.DRAG_AND_DROP_MODE.getStateName(), operationsForDnDController));
        pressMouseResponse();
        dragMouseResponse();
        releaseMouseResponse();
    }

    /**
     * При нажатии мыши выбираем логику в зависимости от состояния
     */
    private void pressMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("textP"))
                    .ifPresent(op -> ((BiArgsOperation) op).mousePressed(transformedX, transformedY));

            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("dndP"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, strokes));

            TextController.changeTextFieldOnText(root, gc, mouseEvent.getX(), mouseEvent.getY());

            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("brushP"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, gc));
        });
    }

    /**
     * При перетаскивании мыши дорисовываем (в режиме кисти) либо перетаскиваем (в режиме Drag&Drop).
     */
    private void dragMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get(MouseEvent.MOUSE_DRAGGED))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, gc));

            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("dndD"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, canvasRedrawer));
        });
    }

    /**
     * При отпускании мыши:
     *  1) В режиме кисти — завершаем штрих и добавляем его в общий список strokes.
     *  2) В режиме drag&drop — «отпускаем» выбранный объект.
     */
    private void releaseMouseResponse() {
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("brushR"))
                    .ifPresent(op -> ((UnaryArgsOperation) op).mouseReleased(strokes));

            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("textR"))
                    .ifPresent(op -> ((QuadroArgsOperation) op).mouseReleased(transformedX, transformedY, root, this));

            Optional.ofNullable(operations.get(CanvasStateController.getState()))
                    .map(ops -> ops.get("dndR"))
                    .ifPresent(op -> ((ZeroArgsOperation) op).mouseReleased());

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
