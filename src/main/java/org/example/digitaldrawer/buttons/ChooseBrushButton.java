package org.example.digitaldrawer.buttons;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

/**
 * Класс, отвечающий за кнопку выбора режима кисти
 */
public class ChooseBrushButton {
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
    public void pressMouseResponse(){
        brushButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
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
