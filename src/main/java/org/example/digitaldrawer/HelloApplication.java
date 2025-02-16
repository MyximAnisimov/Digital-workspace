package org.example.digitaldrawer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
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
        CanvasController canvasController = new CanvasController(1000, 500){
            @Override
            public void paint(GraphicsContext gc) {
                gc.setStroke(Color.BLACK);
                gc.beginPath();
                for(PathElement element : this.getDrawnShapes()){

                    if (element instanceof MoveTo) {
                        MoveTo move = (MoveTo) element;
                        gc.moveTo(move.getX(), move.getY());
                    } else if (element instanceof LineTo) {
                        LineTo line = (LineTo) element;
                        gc.lineTo(line.getX(), line.getY());
                        gc.stroke();
                    }
                }

                gc.setFill(Color.LIGHTGREEN);
                gc.fillOval(60, 10, 180, 180);
                gc.setFill(Color.WHITE);
                gc.fillOval(100, 50, 100, 100);
            }
        };
        canvasController.pressMouseResponse();
        canvasController.dragMouseResponse();
        root.getChildren().add(canvasController);
        root.getChildren().add(border);
        stage.setScene(s);
        stage.show();
        canvasController.redrawCanvas();
    }


    public static void main(String[] args) {

        launch();
    }
}