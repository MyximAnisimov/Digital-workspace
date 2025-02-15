package org.example.exceptions;

public class WrongPenSizeException extends NumberFormatException {
    public WrongPenSizeException(String message) {
        super(message);
    }
}
