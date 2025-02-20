package org.example.digitaldrawer.buttons;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.example.digitaldrawer.controllers.CanvasStateController;
import org.example.digitaldrawer.states.CanvasStates;

/**
 * Класс, отвечающий за кнопку выбора режима кисти
 */
public class ChooseBrushButton implements PressOnMouseEvent{
    private Image image;
    private Button brushButton;
    DropShadow shadow = new DropShadow();

    public ChooseBrushButton(String imageUrl) {
        this.brushButton = new Button();
//        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("999740.png")));
//        this.brushButton.setGraphic(new ImageView(image));

    }

    /**
     * Подсветка кнопки, сигнализирующая о её нажатии
     */
    @Override
    public void pressMouseResponse(){
        brushButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CanvasStateController.setState(CanvasStates.BRUSH_MODE.getStateName());
                brushButton.setEffect(shadow);
            }
        });
    }

    public Button getBrushButton() {
        return brushButton;
    }



    /**
     * Добавить метод, который убирает тень в кнопки, когда нажата другая кнопка
     */
}
