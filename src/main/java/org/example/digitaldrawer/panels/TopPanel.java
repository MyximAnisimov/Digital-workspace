package org.example.digitaldrawer.panels;

import javafx.geometry.Insets;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import org.example.digitaldrawer.buttons.ChooseBrushButton;
import org.example.digitaldrawer.buttons.ChooseTextButton;
import org.example.digitaldrawer.buttons.DragAndDropButton;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;

import java.util.function.UnaryOperator;

/**
 * Класс, отвечающий за верхнюю панель пользователького интерфейса
 */
public final class TopPanel {
    //System.Logger logger = System.getLogger("org.example.digitaldrawer");
    private static final String BACKGROUND_COLOR = "-fx-background-color: #336699;";
    private static final String ICON_URL_PATH = "C:\\Users\\Maxim\\Digital-workspace\\999740.png";
    /**
     * Метод, создающий верхнюю панель с кнопками
     * @return - возвращает верхнюю панель
     */
    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 120, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle(BACKGROUND_COLOR);
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        ChooseBrushButton chooseBrushButton = new ChooseBrushButton(ICON_URL_PATH);
        chooseBrushButton.pressMouseResponse();
        DragAndDropButton dragAndDropButton = new DragAndDropButton();
        dragAndDropButton.pressMouseResponse();
        ChooseTextButton chooseTextButton = new ChooseTextButton();
        chooseTextButton.pressMouseResponse();
            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            PenSizeDropDownList.getPenSize().getEditor().setTextFormatter(textFormatter);
            PenSizeDropDownList.getPenSize().setEditable(true);
            PenSizeDropDownList.getPenSize().setPrefSize(100, 20);
            hbox.getChildren().add(PenSizeDropDownList.getPenSize());
            hbox.getChildren().add(chooseBrushButton.getBrushButton());
            hbox.getChildren().add(dragAndDropButton.getDragAndDropButton());
            hbox.getChildren().add(chooseTextButton.getTextButton());
        return hbox;
    }


}
