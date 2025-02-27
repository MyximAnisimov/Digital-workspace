package org.example.digitaldrawer.controllers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WriteTextController {

    public static void addTextOnCanvas(Group root, TextField textField){
        root.getChildren().add(textField);
    }
}
