package org.example.digitaldrawer.controllers;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

import java.util.ArrayList;
import java.util.List;

public class StrokeShape {

    private final List<PathElement> elements;
    private double brushSize;

    private double offsetX;
    private double offsetY;

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

//    public StrokeShape(){}

    public StrokeShape(double brushSize) {
        this.brushSize = brushSize;
        this.elements = new ArrayList<>();
        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.maxY = Double.MIN_VALUE;
    }

    /**
     * Добавляем элемент (MoveTo или LineTo), параллельно обновляя bounding box
     */
    public void addElement(PathElement element) {
        this.elements.add(element);

        double x, y;
        if (element instanceof MoveTo move) {
            x = move.getX();
            y = move.getY();
        } else if (element instanceof LineTo line) {
            x = line.getX();
            y = line.getY();
        } else {
            return;
        }
        if (x < minX) minX = x;
        if (y < minY) minY = y;
        if (x > maxX) maxX = x;
        if (y > maxY) maxY = y;
    }

    public List<PathElement> getElements() {
        return elements;
    }

    public double getBrushSize() {
        return brushSize;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getMinX() {
        return minX + offsetX;
    }

    public double getMinY() {
        return minY + offsetY;
    }

    public double getMaxX() {
        return maxX + offsetX;
    }

    public double getMaxY() {
        return maxY + offsetY;
    }

    /**
     * Проверяем, попадают ли переданные координаты (x,y) в границы этого штриха.
     * Здесь можно сделать более «тонкую» проверку, но пока ограничимся bounding box.
     */
    public boolean contains(double x, double y) {
        return (x >= getMinX() && x <= getMaxX() &&
                y >= getMinY() && y <= getMaxY());
    }
}

