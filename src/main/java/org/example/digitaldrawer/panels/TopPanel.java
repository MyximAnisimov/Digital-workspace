package org.example.digitaldrawer.panels;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;

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
        PenSizeDropDownList.getPenSize().setPrefSize(100, 20);
        hbox.getChildren().add(PenSizeDropDownList.getPenSize());

        return hbox;
    }


}
