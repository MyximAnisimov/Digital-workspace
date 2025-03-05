package org.example.digitaldrawer.controllers;

import eu.hansolo.tilesfx.events.TreeNodeEvent;


import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import org.example.digitaldrawer.controllers.functionalinterfaces.Operations;
import org.example.digitaldrawer.controllers.handlers.AbstractController;
import org.example.digitaldrawer.controllers.handlers.DnDController;
import org.example.digitaldrawer.states.CanvasStates;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

public class OperationsController<T> {
    private EnumMap<CanvasStates, HashMap<EventType<MouseEvent>, Operations>> operations;
    private HashMap<EventType<MouseEvent>, Operations> textControllerOperations;
    private HashMap<EventType<MouseEvent>, Operations> brushControllerOperations;
    private HashMap<EventType<MouseEvent>, Operations> dndControllerOperations;
    public OperationsController() {
        operations = new EnumMap<>(CanvasStates.class);
        textControllerOperations = new HashMap<>();
        brushControllerOperations = new HashMap<>();
        dndControllerOperations = new HashMap<>();
    }
    public void setTextControllerOperations(EventType<MouseEvent> event, Operations operation) {
        textControllerOperations.put(event, operation);
    }

    public void setBrushControllerOperations(EventType<MouseEvent> event, Operations operation) {
        brushControllerOperations.put(event, operation);
    }

    public void setDndControllerOperations(EventType<MouseEvent> event, Operations operation) {
        dndControllerOperations.put(event, operation);
    }

//    public void setOperations(HashMap<EventType<MouseEvent>, Operations>... controllerOperations) {
//
//    }


}
