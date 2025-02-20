package org.example.digitaldrawer.states;

public enum CanvasStates {
    BRUSH_MODE("Brush mode"),
    DRAG_AND_DROP_MODE("DnD mode");

    private String stateName;
    CanvasStates(String stateName){
        this.stateName = stateName;
    }
    public String getStateName(){
        return stateName;
    }
}
