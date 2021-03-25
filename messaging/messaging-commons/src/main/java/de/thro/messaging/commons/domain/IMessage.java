package de.thro.messaging.commons.domain;

import java.time.LocalDateTime;

public interface IMessage {
    IUser getSender ();
    IUser getReceiver ();
    LocalDateTime getTime();
    String getMessageText();
}
