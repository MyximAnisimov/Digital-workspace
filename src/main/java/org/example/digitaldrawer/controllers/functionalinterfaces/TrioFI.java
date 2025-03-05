package org.example.digitaldrawer.controllers.functionalinterfaces;

@FunctionalInterface
public interface TrioFI<T, U> {

    void apply(T t1, T t2, U u);
}
