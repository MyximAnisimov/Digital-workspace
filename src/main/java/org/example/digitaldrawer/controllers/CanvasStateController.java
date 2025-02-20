package org.example.digitaldrawer.controllers;

import org.example.digitaldrawer.states.CanvasStates;

public class CanvasStateController {
    private static String state = CanvasStates.DRAG_AND_DROP_MODE.getStateName();
//    public CanvasStateController(String state) {
//        CanvasStateController.state = state;
//    }
    public static void setState(String state){
        CanvasStateController.state = state;
    }
    public static String getState(){
        return CanvasStateController.state;
    }
//    public void setCanvasState(String state, CanvasController canvasController){
//        if(CanvasStates.BRUSH_MODE.getStateName().equals(state)){
//            canvasController.pressMouseResponse();
//            canvasController.dragMouseResponse();
//        }
//        else if(CanvasStates.DRAG_AND_DROP_MODE.getStateName().equals(state)){
//            System.out.println("Whoops!");
//        }
//    }
}
