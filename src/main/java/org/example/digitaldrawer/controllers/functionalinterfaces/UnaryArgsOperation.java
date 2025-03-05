package org.example.digitaldrawer.controllers.functionalinterfaces;

import java.util.List;
import java.util.function.Consumer;

public class UnaryArgsOperation<T> extends Operations{
    Consumer<List<T>> operation;

    public UnaryArgsOperation(Consumer<List<T>> operation) {
        this.operation = operation;
    }


//    @Override
    public void mousePressed(T arg1){
        operation.accept((List<T>)arg1);
    }

//    @Override
    public void mouseDragged(T arg1){
        operation.accept((List<T>)arg1);
    }

//    @Override
    public void mouseReleased(T arg1){
        operation.accept((List<T>)arg1);
    }
}
