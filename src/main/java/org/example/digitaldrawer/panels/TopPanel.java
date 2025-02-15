package org.example.digitaldrawer.panels;

import javafx.geometry.Insets;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;

import java.util.function.UnaryOperator;

/**
 * Класс, отвечающий за верхнюю панель пользователького интерфейса
 */
public final class TopPanel {
    //System.Logger logger = System.getLogger("org.example.digitaldrawer");
    private static final String BACKGROUND_COLOR = "-fx-background-color: #336699;";

    /**
     * Метод, создающий верхнюю панель с кнопками
     * @return - возвращает верхнюю панель
     */
    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle(BACKGROUND_COLOR);
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) { // Разрешаем только цифры
                return change;
            }
            return null; // Отклоняем ввод
        };
        try {
            TextFormatter<String> textFormatter = new TextFormatter<>(filter);
            PenSizeDropDownList.getPenSize().getEditor().setTextFormatter(textFormatter);
            PenSizeDropDownList.getPenSize().setEditable(true);
            PenSizeDropDownList.getPenSize().setPrefSize(100, 20);
            hbox.getChildren().add(PenSizeDropDownList.getPenSize());
        }
        catch(WrongThreadException e){
            System.err.println("Wrong pen size!");
        }

        return hbox;
    }


}
