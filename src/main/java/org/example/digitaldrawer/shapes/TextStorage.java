package org.example.digitaldrawer.shapes;

import java.util.List;

public class TextStorage {

    private final List<TextShape> textFileds;

    public TextStorage(List<TextShape> textFileds){
        this.textFileds = textFileds;
    }

    public List<TextShape> getTextFileds(){
        return textFileds;
    }
}
