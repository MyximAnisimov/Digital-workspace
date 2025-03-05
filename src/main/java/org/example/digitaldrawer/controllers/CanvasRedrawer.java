package org.example.digitaldrawer.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.transform.Affine;
import org.example.digitaldrawer.controllers.handlers.BrushController;
import org.example.digitaldrawer.shapes.StrokeShape;

import java.util.List;

public class CanvasRedrawer {
    private final ZoomControl zoomControl;
    private final GraphicsContext gc;

    private final List<StrokeShape> strokes;

    public CanvasRedrawer(List<StrokeShape> strokes, GraphicsContext gc) {
        this.gc = gc;
        this.zoomControl = new ZoomControl();
        this.zoomControl.zoomProperty().addListener(o -> redrawCanvas());
        this.strokes = strokes;
    }

    private final EventHandler<ScrollEvent> zoomHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            Affine affine = gc.getTransform();
            double zoom = affine.getMxx() + scrollEvent.getDeltaY() / 800;
            zoomControl.setZoom(zoom);
        }
    };

    /**
     * Метод, который «перерисовывает» всё на холсте с учётом текущего зума.
     */
    public void redrawCanvas() {
        if (gc == null) {
            return;
        }

        Canvas canvas = gc.getCanvas();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.setTransform(1, 0, 0, 1, 0, 0);
        gc.clearRect(0, 0, w, h);
        double z = zoomControl.getZoom();
        gc.setTransform(z, 0, 0, z, (w - w * z) / 2.0, (h - h * z) / 2.0);

        for (StrokeShape stroke : strokes) {
            BrushController.setBrushSize(gc, stroke.getBrushSize());
            gc.setStroke(Color.BLACK);

            boolean firstMove = true;
            gc.beginPath();

            for (var element : stroke.getElements()) {
                if (element instanceof MoveTo move) {
                    double x = move.getX() + stroke.getOffsetX();
                    double y = move.getY() + stroke.getOffsetY();
                    if (firstMove) {
                        gc.beginPath();
                        firstMove = false;
                    }
                    gc.moveTo(x, y);
                } else if (element instanceof LineTo line) {
                    double x = line.getX() + stroke.getOffsetX();
                    double y = line.getY() + stroke.getOffsetY();
                    gc.lineTo(x, y);
                    gc.stroke();
                }
            }
        }
    }

    public EventHandler<ScrollEvent> getZoomHandler() {
        return zoomHandler;
    }

    /**
     * Класс, отвечающий за зум холста
     */
    private static class ZoomControl {
        private final SimpleDoubleProperty zoom = new SimpleDoubleProperty(1.0);

        public DoubleProperty zoomProperty() {
            return zoom;
        }

        public double getZoom() {
            return zoom.get();
        }

        public void setZoom(double value) {
            if (value <= 0.1) value = 0.1;
            if (value > 10.0) value = 10.0;
            if (value != getZoom()) {
                this.zoom.set(value);
            }
        }
    }
}

