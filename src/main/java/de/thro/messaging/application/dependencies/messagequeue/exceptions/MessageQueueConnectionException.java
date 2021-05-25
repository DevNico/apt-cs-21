package de.thro.messaging.application.dependencies.messagequeue.exceptions;

/**
 * Exception für Fehler während des Nachrichtentransfers durch den Nachrichtenbroker.
 *
 * @author Thomas Linner
 */
public class MessageQueueConnectionException extends Exception {
    /**
     * Constructor - Erstellt eine neue NetworkException
     *
     * @param message Detaillierte Fehlermeldung über die Ursache
     * @param e       Ursprüngliche Exception, welche durch die NetworkException ersetzt werden soll
     */
    public MessageQueueConnectionException(String message, Exception e) {
        super(message, e);
    }
}
