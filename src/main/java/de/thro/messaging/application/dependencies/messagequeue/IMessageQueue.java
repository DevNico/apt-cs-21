package de.thro.messaging.application.dependencies.messagequeue;

import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.domain.models.Message;

import java.util.List;

/**
 * Schnittstelle für einen Nachrichtenbroker. <br>
 * Dieser wird für die Zustellung und den Erhalt von Nachrichten verwendet. <br>
 * <br>
 * Diese Schnittstelle verwendet die {@link AutoCloseable} Schnittstelle, <br>
 * diese bietet eine "close" Methode an, welche zum Trennen der Verbindung verwendet wird. <br>
 * Zusätzlich erlaubt sie das "try-with-resources" - Pattern.
 *
 * @author Thomas Linner
 */
public interface IMessageQueue extends AutoCloseable {

    /**
     * Sende eine Direkt-Nachricht an einen Benutzer.
     *
     * @param message Nachricht welche versendet werden soll <br>
     *                Der Empfänger wird aus der Nachricht ausgelesen
     * @throws MessageQueueConnectionException Nachricht konnte nicht versendet werden:
     *                                         <li> Verbindung zu Nachrichtenbroker konnte nicht hergestellt werden </li>
     *                                         <li> Der angegebene Benutzer konnte nicht gefunden werden </li>
     */
    void sendDirect(Message message) throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException;

    /**
     * Sende eine Rund-Nachricht an alle im Nachrichtenbroker registrierten Benutzer.
     *
     * @param message Nachricht welche versendet werden soll
     * @throws MessageQueueConnectionException Nachricht konnte nicht versendet werden:
     *                                         <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
     */
    void sendBroadcast(Message message) throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException;

    /**
     * Erhalte alle Nachrichten für den im Nachrichtenbroker angemeldeten Benutzer. <br>
     * Die Nachrichten sind nach dem Sende-Zeitstempel sortiert.
     *
     * @return Liste aller erhaltenen Nachrichten seit dem letzten Abruf
     * @throws MessageQueueConnectionException Nachrichten konnten nicht empfangen werden:
     *                                         <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
     */
    List<Message> getDirectMessages(String userName) throws MessageQueueConnectionException, MessageQueueFetchException, MessageQueueConfigurationException;

    List<Message> getBroadcastMessages() throws MessageQueueConnectionException, MessageQueueFetchException, MessageQueueConfigurationException;

}
