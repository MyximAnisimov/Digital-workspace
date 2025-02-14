package org.example.digitaldrawer.controllers;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;

/**
 * Класс, отвечающий за холст пользователя
 */
public class CanvasController {
    private double brushSize = 5.0;
    private final Canvas canvas = new Canvas(1000, 500);
    /**
     * Регистрирует перемещения мыши пользователя над холстом
     * @param gc - объект, необходимый для отображения нарисованных пользователем фигур
     */
    public void pressMouseResponse(GraphicsContext gc) {
        this.canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                gc.beginPath();
                gc.moveTo(mouseEvent.getX(), mouseEvent.getY());
                gc.stroke();
            }
        });
    }

    /**
     * Отрисовывает линии кистью пользователя
     * @param gc  - объект, необходимый для отображения нарисованных пользователем фигур
     */
    public void dragMouseResponse(GraphicsContext gc) {
        this.canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PenSizeDropDownList.getPenSize().valueProperty().addListener((observable, oldValue, newValue) -> {
                    brushSize = newValue.doubleValue();
                });
                setBrushSize(gc, brushSize);
                gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
                gc.stroke();
            }
        });
    }

    /**
     * Устанавливает размер кисти пользователя
     * @param gc - объект, необходимый для отображения нарисованных пользователем фигур
     * @param size - желаемый размер кисти
     */
    public void setBrushSize(GraphicsContext gc, double size) {

        gc.setLineWidth(size);
    }

    /**
     * Геттер
     * @return возвращает текущий холст
     */
    public Canvas getCanvas(){
        return canvas;
    }
}
