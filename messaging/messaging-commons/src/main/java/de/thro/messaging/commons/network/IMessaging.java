package de.thro.messaging.commons.network;

import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;
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
public interface IMessaging extends AutoCloseable {

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
   * @return Liste aller erhaltenen Nachrichten seit dem letzten Abruf
   *
   * @throws NetworkException Nachrichten konnten nicht empfangen werden:
   * <li> Verbindung zum Nachrichtenbroker konnte nicht hergestellt werden </li>
   */
  public List<IMessage> receiveAll() throws NetworkException;
}
