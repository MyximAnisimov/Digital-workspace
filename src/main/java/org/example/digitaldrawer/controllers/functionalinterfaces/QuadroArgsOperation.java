package org.example.digitaldrawer.controllers.functionalinterfaces;

import java.util.function.BiConsumer;

public class QuadroArgsOperation<T, U, B> extends Operations{
    private QuadroFI<T, U, B> operation;

    public QuadroArgsOperation(QuadroFI<T, U, B> operation) {
        this.operation = operation;
    }

//    @Override
    public void mousePressed(T arg1, T arg2, U arg3, B arg4){
        operation.apply(arg1, arg2, arg3, arg4);
    }

    //@Override
    public void mouseDragged(T arg1, T arg2, U arg3, B arg4){
        operation.apply(arg1, arg2, arg3, arg4);
    }

    //@Override
    public void mouseReleased(T arg1, T arg2, U arg3, B arg4){
        operation.apply(arg1, arg2, arg3, arg4);
    }
}
