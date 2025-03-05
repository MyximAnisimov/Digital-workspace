package org.example.digitaldrawer.controllers.functionalinterfaces;

import java.util.function.BiConsumer;

public class BiArgsOperation <T, U> extends Operations{
    private BiConsumer<T, U> operation;

    public BiArgsOperation(BiConsumer<T, U> operation) {
        this.operation = operation;
    }

    public void mousePressed(T arg1, U arg2){
        operation.accept(arg1, arg2);
    }

    public void mouseDragged(T arg1, U arg2){
        operation.accept(arg1, arg2);
    }

    public void mouseReleased(T arg1, U arg2){
        operation.accept(arg1, arg2);
    }
}
