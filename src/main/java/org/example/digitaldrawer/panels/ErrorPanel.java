package org.example.digitaldrawer.panels;

import javafx.scene.control.Alert;
import org.example.digitaldrawer.errors.ErrorTypes;

/**
 * Табличка, высвечивающая информацию пользователю
 */
public class ErrorPanel {
    private static Alert alert = new Alert(Alert.AlertType.INFORMATION);

    /**
     * Позволяет ввести сообщение для вывода на экран пользователя
     * @param title - сообщение
     */
    public static void setAlertTitle(ErrorTypes title) {
        alert.setHeaderText(title.getErrorMessage());
        alert.showAndWait();
    }
}
