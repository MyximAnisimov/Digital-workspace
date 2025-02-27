package org.example.digitaldrawer.buttons;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import org.example.digitaldrawer.controllers.CanvasStateController;
import org.example.digitaldrawer.states.CanvasStates;

public class ChooseTextButton implements PressOnMouseEvent {
    private Button textButton;
    DropShadow shadow = new DropShadow();


    public ChooseTextButton() {
        textButton = new Button("Text");
    }
    /**
     * Подсветка кнопки, сигнализирующая о её нажатии
     */
    @Override
    public void pressMouseResponse(){
        textButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CanvasStateController.setState(CanvasStates.TEXT_MODE.getStateName());
                textButton.setEffect(shadow);
            }
        });
    }
    public Button getTextButton() {
        return textButton;
    }
}
