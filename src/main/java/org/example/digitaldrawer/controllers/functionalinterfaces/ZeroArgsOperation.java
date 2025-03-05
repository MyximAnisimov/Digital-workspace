package org.example.digitaldrawer.controllers.functionalinterfaces;

public class ZeroArgsOperation extends Operations {

    Runnable operation;

    public ZeroArgsOperation(Runnable operation) {
        this.operation = operation;
    }

//    @Override
    public void mousePressed(){
        operation.run();
    }

//    @Override
    public void mouseDragged(){
        operation.run();
    }

//    @Override
    public void mouseReleased(){
        operation.run();
    }
}
