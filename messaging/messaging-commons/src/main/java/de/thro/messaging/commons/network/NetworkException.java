package de.thro.messaging.commons.network;

/**
 * Exception für Fehler während des Nachrichtentransfers durch den Nachrichtenbroker.
 *
 * @author Thomas Linner
 */
public class NetworkException extends Exception {

  /**
   * Constructor - Erstellt eine neue NetworkException
   */
  public NetworkException() {
    super();
  }

  /**
   * Constructor - Erstellt eine neue NetworkException
   *
   * @param message Detaillierte Fehlermeldung über die Ursache
   */
  public NetworkException(String message) {
    super(message);
  }
}
