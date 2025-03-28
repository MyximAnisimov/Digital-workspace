package org.example.digitaldrawer.controllers.functionalinterfaces;

@FunctionalInterface
public interface QuadroFI<T, U, B> {

    void apply(T t1, T t2, U u, B b);
}
