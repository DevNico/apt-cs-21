package de.thro.messaging.commons.domain;

import java.time.LocalDateTime;

public interface IMessage {
    IUser getSender ();
    String getReceiver();

    /**
     * Gibt an ob es sich bei der Nachricht um ein Rundschreiben handelt.
     * @return Gibt True zurück für Rundschreiben und sonst False
     */
    boolean getIsBrodcast();

    /**
     * Gibt die Zeit der Erstellung der Nachricht an
     * @return Zeit der Erstellung der Nachricht als LocalDateTime
     */
    LocalDateTime getTime();
    String getMessageText();
}
