package de.thro.messaging.application.exceptions;

public class ApplicationException extends Exception {

    public ApplicationException(String message, Throwable inner) {
        super(message, inner);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
