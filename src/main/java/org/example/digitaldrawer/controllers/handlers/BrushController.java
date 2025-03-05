package org.example.digitaldrawer.controllers.handlers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import org.example.digitaldrawer.buttons.PenSizeDropDownList;
//import org.example.digitaldrawer.controllers.functionalinterfaces.MainFI;
import org.example.digitaldrawer.errors.ErrorTypes;
import org.example.digitaldrawer.panels.ErrorPanel;
import org.example.digitaldrawer.shapes.StrokeShape;

import java.util.List;
import java.util.Map;

public class BrushController extends AbstractController {

    private static final double DEFAULT_BRUSH_SIZE = 6.0;
    private static final double BRUSH_SIZE_LIMIT = 100;
    private StrokeShape currentStroke = null;
    private static double brushSize = DEFAULT_BRUSH_SIZE;

    public void mousePressed(double transformedX, double transformedY, GraphicsContext gc) {

        if (brushSize == DEFAULT_BRUSH_SIZE) {
            PenSizeDropDownList.getPenSize().getSelectionModel().select(0);
        }

        currentStroke = new StrokeShape(brushSize);
        currentStroke.addElement(new MoveTo(transformedX, transformedY));

        gc.beginPath();
        gc.moveTo(transformedX, transformedY);
        gc.stroke();
    }

    public void mouseDragged(double transformedX, double transformedY, GraphicsContext gc) {
        currentStroke.addElement(new LineTo(transformedX, transformedY));

        gc.lineTo(transformedX, transformedY);
        gc.stroke();
    }

    public void mouseReleased(List<StrokeShape> strokes){
        strokes.add(currentStroke);
        currentStroke = null;
    }

    /**
     * Устанавливает размер кисти пользователя
     *
     * @param gc   - объект, необходимый для отображения нарисованных пользователем фигур
     * @param size - желаемый размер кисти
     */
    public static void setBrushSize(GraphicsContext gc, double size) {
        if (size > BRUSH_SIZE_LIMIT) {
            ErrorPanel.setAlertTitle(ErrorTypes.WRONG_PEN_SIZE);
            return;
        }
        brushSize = size;
        gc.setLineWidth(size);
    }
}
