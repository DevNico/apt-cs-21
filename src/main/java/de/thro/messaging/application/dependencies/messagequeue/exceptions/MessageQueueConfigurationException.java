package de.thro.messaging.application.dependencies.messagequeue.exceptions;

public class MessageQueueConfigurationException extends Exception {
    public MessageQueueConfigurationException(Throwable cause) {
        super(cause);
    }
    public MessageQueueConfigurationException(String error, Throwable cause) {
        super(error, cause);
    }
}
