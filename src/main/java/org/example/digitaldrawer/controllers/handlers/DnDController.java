package org.example.digitaldrawer.controllers.handlers;

import org.example.digitaldrawer.controllers.CanvasRedrawer;
import org.example.digitaldrawer.shapes.StrokeShape;

import java.util.List;

public class DnDController extends AbstractController {

    private StrokeShape selectedStroke = null;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public void mousePressed(double x, double y, List<StrokeShape> strokes){

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

    public void mouseReleased(){
        selectedStroke = null;
    }

    public void mouseDragged(double x, double y, CanvasRedrawer canvasRedrawer){
        double newOffsetX = x - dragOffsetX;
        double newOffsetY = y - dragOffsetY;

        selectedStroke.setOffsetX(newOffsetX);
        selectedStroke.setOffsetY(newOffsetY);
        canvasRedrawer.redrawCanvas();
    }
}
