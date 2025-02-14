package org.example.digitaldrawer.buttons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Класс, предназначенный для кнопки выбора размера кисти
 */
public final class PenSizeDropDownList {
    private static ObservableList<Integer> options =  FXCollections.observableArrayList(
            10,
            12,
            14
    );;
    private static ComboBox<Integer> penSize = new ComboBox<>(options);

    /**
     * Геттер
     * @return возвращает размер шрифта
     */
    public static ComboBox<Integer> getPenSize() {
        return penSize;
    }

}
