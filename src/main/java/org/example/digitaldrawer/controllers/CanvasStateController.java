package org.example.digitaldrawer.controllers;

import org.example.digitaldrawer.states.CanvasStates;

public class CanvasStateController {
    private static String state = CanvasStates.DRAG_AND_DROP_MODE.getStateName();
    public static void setState(String state){
        CanvasStateController.state = state;
    }
    public static String getState(){
        return CanvasStateController.state;
    }
}
