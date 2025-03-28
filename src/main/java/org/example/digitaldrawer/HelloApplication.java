package org.example.digitaldrawer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.digitaldrawer.controllers.canvassettings.CanvasController;
import org.example.digitaldrawer.controllers.handlers.BrushController;
import org.example.digitaldrawer.controllers.handlers.DnDController;
import org.example.digitaldrawer.controllers.handlers.TextController;
import org.example.digitaldrawer.panels.TopPanel;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        TopPanel userPanel = new TopPanel();
        BorderPane border = new BorderPane();
        border.setTop(userPanel.addHBox());
        Group root = new Group();
        Scene s = new Scene(root, 1200, 700, Color.WHITE);
        BrushController brushController = new BrushController();
        TextController textController = new TextController();
        DnDController dnDController = new DnDController();
        CanvasController canvasController = new CanvasController(1200, 700, root, brushController, textController, dnDController);
        root.getChildren().add(canvasController);
        root.getChildren().add(border);
        stage.setScene(s);
        stage.show();
    }


    public static void main(String[] args) {

        launch();
    }
}