package org.example.digitaldrawer.shapes;

import javafx.scene.control.Label;

public class TextShape {
    private final Label text;
    private double offsetX;
    private double offsetY;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    public TextShape(Label label) {
        this.text = label;
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public Label getText() {
        return text;
    }

    public double getMinX() {
        return minX + offsetX;
    }
    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY + offsetY;
    }
    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxX() {
        return maxX + offsetX;
    }
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY + offsetY;
    }
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
}
