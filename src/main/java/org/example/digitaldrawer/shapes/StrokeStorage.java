package org.example.digitaldrawer.shapes;

import java.util.List;

public class StrokeStorage {

    private final List<StrokeShape> strokes;

    public StrokeStorage(List<StrokeShape> strokes){
        this.strokes = strokes;
    }

    public List<StrokeShape> getStrokes(){
        return strokes;
    }
}
