package org.example.exceptions;

public class MissingButtonException extends RuntimeException {
    public MissingButtonException(String message) {
        super(message);
    }
}
