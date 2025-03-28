package org.example.digitaldrawer.controllers.canvassettings;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
import org.example.digitaldrawer.storages.OperationsStorage;
import org.example.digitaldrawer.controllers.handlers.BrushController;
import org.example.digitaldrawer.controllers.handlers.DnDController;
import org.example.digitaldrawer.controllers.EventHandlers;
import org.example.digitaldrawer.controllers.handlers.TextController;
import org.example.digitaldrawer.shapes.StrokeShape;
import org.example.digitaldrawer.shapes.StrokeStorage;

import java.util.*;

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
    private StrokeStorage strokeStorage;

    public CanvasController() {
        this(0, 0, null, null, null, null);
    }

    public CanvasController(double width, double height, Group root, BrushController brushController, TextController textController, DnDController dnDController) {
        super(width, height);
        gc = this.getGraphicsContext2D();
        this.root = root;

        this.brushController = brushController;
        this.dnDController = dnDController;
        this.textController = textController;
        strokeStorage = new StrokeStorage(new ArrayList<>());
        canvasRedrawer = new CanvasRedrawer(strokeStorage.getStrokes(), gc);
        setOnScroll(canvasRedrawer.getZoomHandler());
        OperationsStorage operationsStorage = new OperationsStorage(this.textController, this.brushController, this.dnDController);
        EventHandlers eventHandlers = new EventHandlers(this, gc, this.root, operationsStorage, this.strokeStorage, canvasRedrawer);
        PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
            BrushController.setBrushSize(gc, Double.parseDouble(newValue));
        });
        eventHandlers.pressMouseResponse();
        eventHandlers.dragMouseResponse();
        eventHandlers.releaseMouseResponse();
    }

}
