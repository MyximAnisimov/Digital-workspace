package org.example.digitaldrawer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.digitaldrawer.controllers.CanvasController;
import org.example.digitaldrawer.panels.TopPanel;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        TopPanel userPanel = new TopPanel();
        BorderPane border = new BorderPane();
        border.setTop(userPanel.addHBox());
        Group root = new Group();
        Scene s = new Scene(root, 1000, 500, Color.WHITE);
        CanvasController canvasController = new CanvasController(1000, 500, root);
        root.getChildren().add(canvasController);
        root.getChildren().add(border);
        stage.setScene(s);
        stage.show();
    }


    public static void main(String[] args) {

        launch();
    }
}