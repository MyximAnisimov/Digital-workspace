package org.example.digitaldrawer.buttons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Класс, предназначенный для кнопки выбора размера кисти
 */
public final class PenSizeDropDownList {
    private static final ObservableList<String> options =  FXCollections.observableArrayList(
            "6",
            "8",
            "10",
            "11",
            "12",
            "14",
            "18",
            "24",
            "36",
            "48",
            "72"
    );
    private static final ComboBox<String> penSize = new ComboBox<>(options);
    /**
     * Геттер
     * @return возвращает размер шрифта
     */
    public static ComboBox<String> getPenSize() {
        return penSize;
    }



}
