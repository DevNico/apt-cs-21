package de.thro.messaging.infrastructure.messagequeue;

import com.rabbitmq.client.*;
import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.commons.confighandler.ConfigMessaging;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.commons.serialization.ISerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RabbitMQ Implementierung für die for {@link IMessageQueue} Nachrichtenbroker - Schnittstelle.
 *
 * @author Thomas Linner
 * @see <a href="https://www.rabbitmq.com/">https://www.rabbitmq.com/</a>
 */
public class RabbitMessageQueue implements IMessageQueue {

    private static final String EXCHANGE_DIRECT = "exchange_direct";
    private static final String EXCHANGE_BROADCAST = "exchange_broadcast";

    private final ISerializer<Message> serializer;

    private final Connection connection;
    private final Channel channel;

    /**
     * Constructor - Erstellt einen neuen RabbitMQ Nachrichtenbroker.
     *
     * @param config     Konfigurationscontainer für die Einrichtung der Remote - Verbindung
     * @param serializer Der RabbitMQ Nachrichtenbroker arbeitet auf String-Nachrichten. <br>
     *                   Der Serializer wird für die Konvertierung zwischen {@link Message} und {@link String} verwendet.
     * @throws MessageQueueConnectionException Fehler beim Aufbau der Verbindung:
     *                          <li> URI konnte nicht konfiguriert werden </li>
     *                          <li> Verbindung / Kanal konnte nicht geöffnet werden </li>
     */
    public RabbitMessageQueue(ConfigMessaging config, ISerializer<Message> serializer) throws MessageQueueConnectionException {
        this.serializer = serializer;

        String uri = String.format("amqp://%s:%s@%s:%s",
                config.getUsername(), config.getPassword(), config.getIp(), "5672"/*config.getPort()*/);

        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUri(uri);

            this.connection = connectionFactory.newConnection();
            this.channel = this.connection.createChannel();

            this.setupExchanges();
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new MessageQueueConnectionException("Error setting uri: " + uri, e);
        } catch (IOException | TimeoutException e) {
            throw new MessageQueueConnectionException("Error opening connection / channel to uri: " + uri, e);
        }
    }

    /**
     * Erstellt und konfiguriert die beiden benötigten Exchanges für den Nachrichten-Austausch. <br>
     * Falls die beiden Exchanges bereits existieren, erfolgt keine Aktion.
     * <li> Direktnachricht -> EXCHANGE_DIRECT </li>
     * <li> Rundnachricht   -> EXCHANGE_BROADCAST </li>
     *
     * @throws MessageQueueConnectionException Fehler beim Erstellen der Exchanges
     */
    private void setupExchanges() throws MessageQueueConnectionException {
        try {
            // Wir können die Methode "exchangeDeclare" hier sicher aufrufen:
            // - Exchange noch nicht vorhanden -> Exchange wird angelegt
            // - Exchange bereits vorhanden    -> Methode führt keine Aktion aus
            this.channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true, false, null);
            this.channel.exchangeDeclare(EXCHANGE_BROADCAST, BuiltinExchangeType.FANOUT, true, false, null);
        } catch (IOException e) {
            throw new MessageQueueConnectionException("Error creating exchange", e);
        }
    }

    /**
     * Registriert einen Benutzer im Nachrichtenbroker. <br>
     * Dies erstellt eine neue Queue und verbindet diese mit den entsprechenden Exchanges. <br>
     * Für einen Professor wird keine Queue angelegt, da dieser keine Nachrichten empfangen kann.
     *
     * @param user Der Benutzer, welcher im Broker registriert werden soll
     * @throws MessageQueueConnectionException Fehler beim Erstellen oder Verbinden der Queue
     */
    private void registerUser(User user) throws MessageQueueConnectionException {

        try {
            String queueName = user.getName();

            // Falls bereits eine Queue mit dem Namen vorhanden ist, erfolgt keine Aktion
            this.channel.queueDeclare(queueName, true, false, false, null);

            // Verbinde Queue mit Exchange für Direkt- und Rundnachrichten
            // Falls User vom Typ Professor ist, binde die Queue nur mit dem Exchange für Direktnachrichten an.
            if (user.getUserType() == UserType.TEACHER) {
                this.channel.queueBind(queueName, EXCHANGE_DIRECT, queueName);
            } else {
                this.channel.queueBind(queueName, EXCHANGE_DIRECT, queueName);
                this.channel.queueBind(queueName, EXCHANGE_BROADCAST, "");
            }
        } catch (IOException e) {
            throw new MessageQueueConnectionException("Error creating or binding queue for user: " + user.getName(), e);
        }
    }

    /**
     * Diese Funktion wird verwendet um den AMQP Fehlercode aus einer Exception auszulesen. <br>
     * Wird der Fehler nicht durch einen AMQP Fehler erzeugt, gibt die Methode -1 zurück. <br>
     * Fehlerkonstanten: {@link AMQP}
     *
     * @param e Der aufgetretene Fehler, dieser sollte durch einen AMQP Fehler erzeugt sein
     * @return Ausgelesender AMQP Fehlercode <br>
     * Im Fehlerfall wird -1 zurückgegeben.
     */
    private int parseErrorCode(Exception e) {
        String regex = "(\\w+)=([^\\D]+)";

        // Prüfe ob die Fehlerursache eine ShutdownSignalException ist
        // Diese tritt bei Fehlern während des Zugriffs auf den AMQP-Broker auf
        if (!(e.getCause() instanceof ShutdownSignalException))
            return -1;

        // Konfiguriere REGEX um den Fehlercode zu finden
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(e.getCause().getMessage());

        // Suche Fehlercode in Fehlernachricht
        while (matcher.find())
            if (matcher.group().startsWith("code"))
                return Integer.parseInt(matcher.group().split("=")[1]);

        return -1;
    }

    /**
     * Sende eine Direkt-Nachricht an einen Benutzer.
     *
     * @param message Nachricht welche versendet werden soll <br>
     *                Der Empfänger wird aus der Nachricht ausgelesen
     * @throws MessageQueueConnectionException Nachricht konnte nicht versendet werden:
     *                          <li> Verbindung zu Nachrichtenbroker konnte nicht hergestellt werden </li>
     *                          <li> Der angegebene Benutzer konnte nicht gefunden werden </li>
     */
    @Override
    public void sendDirect(Message message) throws MessageQueueConnectionException, MessageQueueSendException {
        try {
            this.channel.basicPublish(EXCHANGE_DIRECT, message.getReceiver(), null,
                    this.serializer.serialize(message).getBytes());
        } catch (IOException e) {
            int errorCode = this.parseErrorCode(e);

            switch (errorCode) {
                case AMQP.CONNECTION_FORCED:
                    throw new MessageQueueConnectionException("The messaging broker can not be reached", e);
                case AMQP.NOT_FOUND:
                    //throw new MessageQueueConnectionException("The given user does not exist: " + user.getName(), e);
                    throw new MessageQueueSendException(e);
                default:
                    throw new MessageQueueConnectionException("Unidentified network error occurred", e);
            }
        }
    }

    /**
     * Sende eine Rund-Nachricht an alle im Nachrichtenbroker registrierten Benutzer.
     *
     * @param message Nachricht welche versendet werden soll
     * @throws MessageQueueConnectionException Nachricht konnte nicht versendet werden:
     *                          <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
     */
    @Override
    public void sendBroadcast(Message message) throws MessageQueueConnectionException {
        try {
            this.channel.basicPublish(EXCHANGE_BROADCAST, "", null,
                    this.serializer.serialize(message).getBytes());
        } catch (IOException e) {
            int errorCode = this.parseErrorCode(e);

            switch (errorCode) {
                case AMQP.CONNECTION_FORCED:
                    throw new MessageQueueConnectionException("The messaging broker can not be reached", e);
                default:
                    throw new MessageQueueConnectionException("Unidentified network error occurred", e);
            }
        }
    }

    /**
     * Erhalte alle Nachrichten für den im Nachrichtenbroker angemeldeten Benutzer. <br>
     * Die Nachrichten sind nach dem Sende-Zeitstempel sortiert.
     *
     * @return Liste aller erhaltenen Nachrichten seit dem letzten Abruf
     * @throws MessageQueueConnectionException Nachrichten konnten nicht empfangen werden:
     *                          <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
     */
    @Override
    public List<Message> getDirectMessages(String userName) throws MessageQueueConnectionException {
        // Das TreeSet wird verwendet um die Nachrichten beim Hinzufügen automatisch
        // nach ihrem Zeitstempel zu sortieren
        Set<Message> messages = new TreeSet<>(Comparator.comparing(Message::getTime));

        String queueName = userName;

        try {
            // Wiederhole solange sich noch Nachrichten in der Queue des Brokers befinden
            while (this.channel.messageCount(queueName) > 0) {
                // Erhalte Nachricht und konvertiere in IMessaging
                GetResponse response = this.channel.basicGet(queueName, true);
                Message message = this.serializer.deserialize(new String(response.getBody()));

                messages.add(message);
            }
        } catch (IOException e) {
            int errorCode = this.parseErrorCode(e);

            switch (errorCode) {
                case AMQP.CONNECTION_FORCED:
                    throw new MessageQueueConnectionException("The messaging broker can not be reached", e);
                default:
                    throw new MessageQueueConnectionException("Unidentified network error occurred", e);
            }
        }

        // Erzeuge eine neue Liste mit den sortierten Elementen aus dem TreeSet
        // und gebe diese zurück
        return new ArrayList<>(messages);
    }

    /**
     * Schließt die Verbindung zum Nachrichtenbroker.
     *
     * @throws MessageQueueConnectionException Fehler beim Schließen der Verbindung
     */
    @Override
    public void close() throws MessageQueueConnectionException {
        try {
            this.channel.close();
            this.connection.close();
        } catch (IOException | TimeoutException e) {
            throw new MessageQueueConnectionException("Error closing channel / connection", e);
        }
    }

    @Override
    public List<Message> getBroadcastMessages() throws MessageQueueConnectionException, MessageQueueFetchException, MessageQueueConfigurationException {
        return new ArrayList<>();
    }
}
