package de.thro.messaging.commons.network;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.ShutdownSignalException;
import de.thro.messaging.commons.confighandler.ConfigMessaging;
import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;
import de.thro.messaging.commons.serialization.ISerializer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RabbitMQ Implementierung für die for {@link IMessaging} Nachrichtenbroker - Schnittstelle.
 *
 * @see <a href="https://www.rabbitmq.com/">https://www.rabbitmq.com/</a>
 *
 * @author Thomas Linner
 */
public class MessagingRabbitMQ implements IMessaging {

  private static final String EXCHANGE_DIRECT = "exchange_direct";
  private static final String EXCHANGE_BROADCAST = "exchange_broadcast";

  private final IUser user;
  private final ISerializer<IMessage> serializer;

  private final Connection connection;
  private final Channel channel;

  /**
   * Constructor - Erstellt einen neuen RabbitMQ Nachrichtenbroker.
   *
   * @param config     Konfigurationscontainer für die Einrichtung der Remote - Verbindung
   * @param user       Benutzer, welcher im Nachrichtenbroker registriert / angemeldet werden soll
   * @param serializer Der RabbitMQ Nachrichtenbroker arbeitet auf String-Nachrichten. <br>
   *                   Der Serializer wird für die Konvertierung zwischen {@link IMessage} und {@link String} verwendet.
   *
   * @throws NetworkException Fehler beim Aufbau der Verbindung:
   * <li> URI konnte nicht konfiguriert werden </li>
   * <li> Verbindung / Kanal konnte nicht geöffnet werden </li>
   */
  public MessagingRabbitMQ(ConfigMessaging config, IUser user, ISerializer<IMessage> serializer) throws NetworkException {
    this.user = user;
    this.serializer = serializer;

    String uri = String.format("amqp://%s:%s@%s:%s",
        config.getUsername(), config.getPassword(), config.getIp(), "5672"/*config.getPort()*/);

    try {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri(uri);

      this.connection = connectionFactory.newConnection();
      this.channel = this.connection.createChannel();

      this.setupExchanges();
      this.registerUser(this.user);
    } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
      throw new NetworkException("Error setting uri: " + uri, e);
    } catch (IOException | TimeoutException e) {
      throw new NetworkException("Error opening connection / channel to uri: " + uri, e);
    }
  }

  /**
   * Erstellt und konfiguriert die beiden benötigten Exchanges für den Nachrichten-Austausch. <br>
   * Falls die beiden Exchanges bereits existieren, erfolgt keine Aktion.
   * <li> Direktnachricht -> EXCHANGE_DIRECT </li>
   * <li> Rundnachricht   -> EXCHANGE_BROADCAST </li>
   *
   * @throws NetworkException Fehler beim Erstellen der Exchanges
   */
  private void setupExchanges() throws NetworkException {
    try {
      // Wir können die Methode "exchangeDeclare" hier sicher aufrufen:
      // - Exchange noch nicht vorhanden -> Exchange wird angelegt
      // - Exchange bereits vorhanden    -> Methode führt keine Aktion aus
      this.channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true, false, null);
      this.channel.exchangeDeclare(EXCHANGE_BROADCAST, BuiltinExchangeType.FANOUT, true, false, null);
    } catch (IOException e) {
      throw new NetworkException("Error creating exchange", e);
    }
  }

  /**
   * Registriert einen Benutzer im Nachrichtenbroker. <br>
   * Dies erstellt eine neue Queue und verbindet diese mit den entsprechenden Exchanges. <br>
   * Für einen Professor wird keine Queue angelegt, da dieser keine Nachrichten empfangen kann.
   *
   * @param user Der Benutzer, welcher im Broker registriert werden soll
   *
   * @throws NetworkException Fehler beim Erstellen oder Verbinden der Queue
   */
  private void registerUser(IUser user) throws NetworkException {
    // Wir benötigen keine Queue für einen Professor, da dieser keine Nachrichten erhalten kann
    if (user.getUserType() == UserType.TEACHER)
      return;

    try {
      String queueName = user.getName();

      // Falls bereits eine Queue mit dem Namen vorhanden ist, erfolgt keine Aktion
      this.channel.queueDeclare(queueName, true, false, false, null);

      // Verbinde Queue mit Exchange für Direkt- und Rundnachrichten
      this.channel.queueBind(queueName, EXCHANGE_DIRECT, queueName);
      this.channel.queueBind(queueName, EXCHANGE_BROADCAST, "");
    } catch (IOException e) {
      throw new NetworkException("Error creating or binding queue for user: " + user.getName(), e);
    }
  }

  /**
   * Diese Funktion wird verwendet um den AMQP Fehlercode aus einer Exception auszulesen. <br>
   * Wird der Fehler nicht durch einen AMQP Fehler erzeugt, gibt die Methode -1 zurück. <br>
   * Fehlerkonstanten: {@link AMQP}
   *
   * @param e Der aufgetretene Fehler, dieser sollte durch einen AMQP Fehler erzeugt sein
   *
   * @return Ausgelesender AMQP Fehlercode <br>
   *         Im Fehlerfall wird -1 zurückgegeben.
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
   *
   * @throws NetworkException Nachricht konnte nicht versendet werden:
   * <li> Verbindung zu Nachrichtenbroker konnte nicht hergestellt werden </li>
   * <li> Der angegebene Benutzer konnte nicht gefunden werden </li>
   */
  @Override
  public void sendDirect(IMessage message) throws NetworkException {
    try {
      this.channel.basicPublish(EXCHANGE_DIRECT, message.getReceiver(), null,
          this.serializer.serialize(message).getBytes());
    } catch (IOException e) {
      int errorCode = this.parseErrorCode(e);

      switch (errorCode) {
        case AMQP.CONNECTION_FORCED: throw new NetworkException("The messaging broker can not be reached", e);
        case AMQP.NOT_FOUND:         throw new NetworkException("The given user does not exist: " + user.getName(), e);
        default:                     throw new NetworkException("Unidentified network error occurred", e);
      }
    }
  }

  /**
   * Sende eine Rund-Nachricht an alle im Nachrichtenbroker registrierten Benutzer.
   *
   * @param message Nachricht welche versendet werden soll
   *
   * @throws NetworkException Nachricht konnte nicht versendet werden:
   * <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
   */
  @Override
  public void sendBroadcast(IMessage message) throws NetworkException {
    try {
      this.channel.basicPublish(EXCHANGE_BROADCAST, "", null,
          this.serializer.serialize(message).getBytes());
    } catch (IOException e) {
      int errorCode = this.parseErrorCode(e);

      switch (errorCode) {
        case AMQP.CONNECTION_FORCED: throw new NetworkException("The messaging broker can not be reached", e);
        default:                     throw new NetworkException("Unidentified network error occurred", e);
      }
    }
  }

  /**
   * Erhalte alle Nachrichten für den im Nachrichtenbroker angemeldeten Benutzer. <br>
   * Die Nachrichten sind nach dem Sende-Zeitstempel sortiert.
   *
   * @return Liste aller erhaltenen Nachrichten seit dem letzten Abruf
   *
   * @throws NetworkException Nachrichten konnten nicht empfangen werden:
   * <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
   */
  @Override
  public List<IMessage> receiveAll() throws NetworkException {
    // Das TreeSet wird verwendet um die Nachrichten beim Hinzufügen automatisch
    // nach ihrem Zeitstempel zu sortieren
    Set<IMessage> messages = new TreeSet<>(Comparator.comparing(IMessage::getTime));

    String queueName = this.user.getName();

    try {
      // Wiederhole solange sich noch Nachrichten in der Queue des Brokers befinden
      while (this.channel.messageCount(queueName) > 0) {
        // Erhalte Nachricht und konvertiere in IMessaging
        GetResponse response = this.channel.basicGet(queueName, true);
        IMessage message = this.serializer.deserialize(new String(response.getBody()));

        messages.add(message);
      }
    } catch (IOException e) {
      int errorCode = this.parseErrorCode(e);

      switch (errorCode) {
        case AMQP.CONNECTION_FORCED: throw new NetworkException("The messaging broker can not be reached", e);
        default:                     throw new NetworkException("Unidentified network error occurred", e);
      }
    }

    // Erzeuge eine neue Liste mit den sortierten Elementen aus dem TreeSet
    // und gebe diese zurück
    return new ArrayList<>(messages);
  }

  /**
   * Schließt die Verbindung zum Nachrichtenbroker.
   *
   * @throws NetworkException Fehler beim Schließen der Verbindung
   */
  @Override
  public void close() throws NetworkException {
    try {
      this.channel.close();
      this.connection.close();
    } catch (IOException | TimeoutException e) {
      throw new NetworkException("Error closing channel / connection", e);
    }
  }
}
