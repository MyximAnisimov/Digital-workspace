package org.example.digitaldrawer.controllers.handlers;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.digitaldrawer.controllers.EventHandlers;
import org.example.digitaldrawer.controllers.canvassettings.CanvasStateController;
import org.example.digitaldrawer.shapes.TextShape;
import org.example.digitaldrawer.states.CanvasStates;

import java.util.HashMap;
import java.util.Map;

public class TextController{
    private static final HashMap<TextShape, TextArea> activeAndPassiveTexts = new HashMap<>();
    private TextShape textShape = null;
    private Label label = null;
    private TextArea newTextField = null;


    public static void addTextOnCanvas(Group root, TextArea textField, TextShape textShape) {
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

    public static void changeFiledOnText(Group root, GraphicsContext gc, double x, double y){
        for(Map.Entry<TextShape, TextArea> element : activeAndPassiveTexts.entrySet()) {
//            TextArea textArea = new TextArea();
//            textArea.setText(element.getValue().getText());

            root.getChildren().add(element.getValue());
//            gc.fillText(element.getValue().getText(), x, y);
        }
    }

    public void mousePressed(double x, double y, double[] arr){
        textShape = new TextShape(new Label(""));
        textShape.setMaxX(x);
        textShape.setMaxY(y);
        arr[0] = x;
        arr[1] = y;
    }
    public void mouseDragged(){}

    public void mouseReleased(double x, double y, Group root, EventHandlers canvasController){
        newTextField = new TextArea();
        newTextField.setWrapText(true);
        newTextField.requestFocus();
        newTextField.setLayoutY(textShape.getMaxY());

        if(x > textShape.getMaxX()){
            newTextField.setLayoutX(textShape.getMaxX());
            newTextField.setPrefWidth(x - textShape.getMaxX());
            textShape.setMinX(textShape.getMaxX());
            textShape.setMaxX(x);
        }
        else if(x > textShape.getMaxX()){
            newTextField.setLayoutX(x);
            newTextField.setPrefWidth(textShape.getMaxX() - x);
            textShape.setMinX(x);
            textShape.setMaxX(textShape.getMaxX());
        }

        if(y > textShape.getMaxY()){
            newTextField.setPrefHeight(y - textShape.getMaxY());
            textShape.setMinY(textShape.getMaxY());
            textShape.setMaxY(y);
        }
        else if(y < textShape.getMaxY()){
            newTextField.setPrefHeight(textShape.getMaxY() - y);
            textShape.setMinY(y);
            textShape.setMaxY(textShape.getMaxY());
        }

        newTextField.setStyle("-fx-padding: 0 0 0 0; -fx-alignment: TOP_LEFT;");
        label = new Label(newTextField.getText());
        textShape = new TextShape(label);
        TextController.addTextOnCanvas(root, newTextField, textShape);
        CanvasStateController.setState(CanvasStates.DRAG_AND_DROP_MODE.getStateName());
    }
}
