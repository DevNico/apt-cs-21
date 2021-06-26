package de.thro.messaging.application.dependencies.messagequeue.exceptions;

public class MessageQueueFetchException extends Exception {
    public MessageQueueFetchException(String s, Throwable cause) {
        super(cause);
    }
}
