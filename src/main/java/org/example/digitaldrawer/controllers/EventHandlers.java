package org.example.digitaldrawer.controllers;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.example.digitaldrawer.controllers.canvassettings.CanvasRedrawer;
import org.example.digitaldrawer.controllers.canvassettings.CanvasStateController;
import org.example.digitaldrawer.controllers.handlers.TextController;
import org.example.digitaldrawer.storages.OperationsStorage;
import org.example.digitaldrawer.controllers.functionalinterfaces.*;
import org.example.digitaldrawer.shapes.StrokeStorage;

import java.util.Optional;

public class EventHandlers {
    private final GraphicsContext gc;
    private final Group root;
    private final Canvas canvas;
    private final OperationsStorage operationsStorage;
    private double[] arr = new double[2];
    private StrokeStorage strokeStorage;
    private CanvasRedrawer canvasRedrawer;
    public EventHandlers(Canvas canvas, GraphicsContext gc, Group root, OperationsStorage operationsStorage, StrokeStorage strokeStorage, CanvasRedrawer canvasRedrawer){
        this.gc = gc;
        this.root = root;
        this.canvas = canvas;
        this.operationsStorage = operationsStorage;
        this.strokeStorage = strokeStorage;
        this.canvasRedrawer = canvasRedrawer;
    }

    public GraphicsContext getGc(){
        return gc;
    }

    /**
     * При нажатии мыши выбираем логику в зависимости от состояния
     */
    public void pressMouseResponse() {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            if(mouseEvent.getClickCount() == 2){
                TextController.changeFiledOnText(root, gc, transformedX, transformedY);
            }
            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("textP"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, arr));

            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("dndP"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, strokeStorage.getStrokes()));

            TextController.changeTextFieldOnText(root, this.getGc(), arr[0], arr[1]);

            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("brushP"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, gc));
        });
    }

    /**
     * При перетаскивании мыши дорисовываем (в режиме кисти) либо перетаскиваем (в режиме Drag&Drop).
     */
    public void dragMouseResponse() {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("brushD"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, gc));

            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("dndD"))
                    .ifPresent(op -> ((TrioArgsOperation) op).mousePressed(transformedX, transformedY, canvasRedrawer));
        });
    }

    /**
     * При отпускании мыши:
     *  1) В режиме кисти — завершаем штрих и добавляем его в общий список strokes.
     *  2) В режиме drag&drop — «отпускаем» выбранный объект.
     */
    public void releaseMouseResponse() {
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            double[] transformedCoords = transformCoordinates(mouseEvent.getX(), mouseEvent.getY());
            double transformedX = transformedCoords[0];
            double transformedY = transformedCoords[1];
            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("brushR"))
                    .ifPresent(op -> ((UnaryArgsOperation) op).mouseReleased(strokeStorage.getStrokes()));

            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
                    .map(ops -> ops.get("textR"))
                    .ifPresent(op -> ((QuadroArgsOperation) op).mouseReleased(transformedX, transformedY, root, this));

            Optional.ofNullable(operationsStorage.getOperations().get(CanvasStateController.getState()))
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
            Affine inverseTransform = this.getGc().getTransform().createInverse();
            double[] coords = new double[] { x, y };
            inverseTransform.transform2DPoints(coords, 0, coords, 0, 1);
            return coords;
        } catch (NonInvertibleTransformException e) {
            System.err.println("Impossible to convert coordinates!");
            return new double[] { x, y };
        }
    }

}
