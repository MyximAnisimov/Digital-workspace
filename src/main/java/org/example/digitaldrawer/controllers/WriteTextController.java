package org.example.digitaldrawer.controllers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class WriteTextController {
    private static final HashMap<TextShape, TextArea> activeAndPassiveTexts = new HashMap<>();

    public static void addTextOnCanvas(Group root, TextArea textField, CanvasController canvasController,TextShape textShape) {
        activeAndPassiveTexts.put(textShape, textField);
        root.getChildren().add(textField);
    }

    /**
     * Основна идея реализации данного метода:
     * у нас есть нажатый элемент. нам нужно сделать так, чтобы после нажатия на какой-то из
     * элементов произошла смена предыдущего текстового поля на лейбл
     * @param root
     * @param x
     * @param y
     */
    public static void changeTextFieldOnText(Group root, GraphicsContext gc, double x, double y){
        for(Map.Entry<TextShape, TextArea> element : activeAndPassiveTexts.entrySet()) {
            root.getChildren().remove(element.getValue());
                gc.fillText(element.getValue().getText(), x, y);
        }
    }
}
