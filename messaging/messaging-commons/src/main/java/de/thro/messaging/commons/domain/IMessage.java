package de.thro.messaging.commons.domain;

import java.time.LocalDateTime;

public interface IMessage {
    IUser getSender ();
    String getReciever ();
    LocalDateTime getTime();
    String getMessageText();
}
