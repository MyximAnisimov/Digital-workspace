package org.example.digitaldrawer.errors;

/**
 * Класс, для хранения сообщений о различных предупреждениях/ошибках
 */
public enum ErrorTypes {
    WRONG_PEN_SIZE("Choose pen size between 1 and 100!");
    private String errorMessage;
    ErrorTypes(String message) {
        this.errorMessage = message;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
