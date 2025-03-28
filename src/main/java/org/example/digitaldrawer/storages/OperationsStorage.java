package org.example.digitaldrawer.storages;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import org.example.digitaldrawer.controllers.canvassettings.CanvasRedrawer;
import org.example.digitaldrawer.controllers.functionalinterfaces.*;
import org.example.digitaldrawer.controllers.handlers.BrushController;
import org.example.digitaldrawer.controllers.handlers.DnDController;
import org.example.digitaldrawer.controllers.EventHandlers;
import org.example.digitaldrawer.controllers.handlers.TextController;
import org.example.digitaldrawer.shapes.StrokeShape;
import org.example.digitaldrawer.states.CanvasStates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class OperationsStorage {
    private final HashMap<String, HashMap<String, Operations>> operations = new HashMap<>();
    private HashMap<String, Operations> operationsForTextController = new HashMap<>();
    private HashMap<String, Operations> operationsForBrushController = new HashMap<>();
    private HashMap<String, Operations> operationsForDnDController = new HashMap<>();
    private TrioFI<Double, double[]> mousePressedForTextController;
    private QuadroFI<Double, Group, EventHandlers> mouseReleasedForText;
    private TrioFI<Double, GraphicsContext> mouseDraggedForBrush;
    private TrioFI<Double, GraphicsContext> mousePressedForBrush;
    private TrioFI<Double, List<StrokeShape>> mousePresssedForDnD;
    private TrioFI<Double, CanvasRedrawer> mouseDraggedForDnd;
    private Consumer<List<StrokeShape>> mouseReleasedForBrush;
    private Runnable mouseReleasedForDnd;

    public OperationsStorage(TextController textController, BrushController brushController, DnDController dnDController) {
        mousePressedForTextController = textController::mousePressed;
        mouseReleasedForText = textController::mouseReleased;
        mouseDraggedForBrush = brushController::mouseDragged;
        mousePressedForBrush = brushController::mousePressed;
        mouseReleasedForBrush = brushController::mouseReleased;
        mousePresssedForDnD = dnDController::mousePressed;
        mouseReleasedForDnd = dnDController::mouseReleased;
        mouseDraggedForDnd = dnDController::mouseDragged;
        operationsForTextController.putAll(Map.of("textP", new TrioArgsOperation<>(mousePressedForTextController), "textR", new QuadroArgsOperation<>(mouseReleasedForText)));
        operationsForBrushController.putAll(Map.of("brushP", new TrioArgsOperation<>(mousePressedForBrush), "brushD", new TrioArgsOperation<>(mouseDraggedForBrush), "brushR", new UnaryArgsOperation<>(mouseReleasedForBrush)));
        operationsForDnDController.putAll(Map.of("dndP", new TrioArgsOperation<>(mousePresssedForDnD), "dndR", new ZeroArgsOperation(mouseReleasedForDnd), "dndD", new TrioArgsOperation<>(mouseDraggedForDnd)));
        operations.putAll(Map.of(CanvasStates.TEXT_MODE.getStateName(), operationsForTextController, CanvasStates.BRUSH_MODE.getStateName(), operationsForBrushController, CanvasStates.DRAG_AND_DROP_MODE.getStateName(), operationsForDnDController));
    }

    public HashMap<String, HashMap<String, Operations>> getOperations(){
        return operations;
    }
}
