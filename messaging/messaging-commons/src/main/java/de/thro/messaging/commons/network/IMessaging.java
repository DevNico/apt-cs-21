package de.thro.messaging.commons.network;

/**
 * Schnittstelle für einen Nachrichtenbroker. <br>
 * Dieser wird für die Zustellung und den Erhalt von Nachrichten verwendet.
 *
 * @author Thomas Linner
 */
public interface IMessaging {

  /**
   * Sende eine Direkt-Nachricht an einen angegebenen Benutzer.
   *
   * @param user    Ziel-Benutzer für die Direkt-Nachricht
   * @param message Nachricht welche versendet werden soll
   *
   * @throws NetworkException Nachricht konnte nicht versendet werden:
   * <li> Verbindung zu Nachrichtenbroker konnte nicht hergestellt werden </li>
   * <li> Der angegebene Benutzer konnte nicht gefunden werden </li>
   */
  public void sendDirect(IUser user, IMessage message) throws NetworkException;

  /**
   * Sende eine Rund-Nachricht an alle im Nachrichtenbroker registrierten Benutzer.
   *
   * @param message Nachricht welche versendet werden soll
   *
   * @throws NetworkException Nachricht konnte nicht versendet werden:
   * <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
   */
  public void sendBroadcast(IMessage message) throws NetworkException;

  /**
   * Erhalte alle Nachrichten für den im Nachrichtenbroker angemeldeten Benutzer. <br>
   * Die Nachrichten sind nach dem Sende-Zeitstempel sortiert.
   *
   * @return Liste aller erhaltenen Nachrichten
   */
  public List<IMessage> receiveAll();
}
