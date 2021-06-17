package de.thro.messaging.application.dependencies.messagequeue.exceptions;

public class MessageQueueSendException extends Exception {
    public MessageQueueSendException(Throwable cause) {
        super(cause);
    }
    public MessageQueueSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
