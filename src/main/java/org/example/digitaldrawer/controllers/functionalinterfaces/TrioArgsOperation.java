package org.example.digitaldrawer.controllers.functionalinterfaces;

public class TrioArgsOperation<T, U> extends Operations{

    TrioFI<T, U> operation = null;

    public TrioArgsOperation(TrioFI<T, U> operation) {
        this.operation = operation;
    }

//    @Override
    public void mousePressed(T arg1, T arg2, U arg3) {
        operation.apply(arg1, arg2, arg3);
    }

//    @Override
    public void mouseDragged(T arg1, T arg2, U arg3) {
        operation.apply(arg1, arg2, arg3);
    }

//    @Override
    public void mouseReleased(T arg1, T arg2, U arg3) {
        operation.apply(arg1, arg2, arg3);
    }
}
