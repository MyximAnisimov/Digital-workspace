package org.example.digitaldrawer.controllers;

import javafx.scene.canvas.GraphicsContext;
import org.example.digitaldrawer.errors.ErrorTypes;
import org.example.digitaldrawer.panels.ErrorPanel;

public class BrushSizeController {
    private static final double DEFAULT_BRUSH_SIZE = 6.0;
    private static final double BRUSH_SIZE_LIMIT = 100;
    /**
     * Устанавливает размер кисти пользователя
     *
     * @param gc   - объект, необходимый для отображения нарисованных пользователем фигур
     * @param size - желаемый размер кисти
     */
    public static double setBrushSize(GraphicsContext gc, double size) {
        if (size > BRUSH_SIZE_LIMIT) {
            ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
            return DEFAULT_BRUSH_SIZE;
        }
        gc.setLineWidth(size);
        return size;
    }
}
