package org.example.digitaldrawer.buttons;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import org.example.digitaldrawer.controllers.CanvasStateController;
import org.example.digitaldrawer.states.CanvasStates;

public class DragAndDropButton implements PressOnMouseEvent{
    private Button dragAndDropButton;
    DropShadow shadow = new DropShadow();

    public DragAndDropButton() {
        this.dragAndDropButton = new Button();

    }

    /**
     * Подсветка кнопки, сигнализирующая о её нажатии
     */
    @Override
    public void pressMouseResponse(){
        dragAndDropButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CanvasStateController.setState(CanvasStates.DRAG_AND_DROP_MODE.getStateName());
                dragAndDropButton.setEffect(shadow);
            }
        });
    }

    public Button getDragAndDropButton() {
        return dragAndDropButton;
    }



}
