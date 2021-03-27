package de.thro.messaging.commons.domain.impl;

import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;

import java.time.LocalDateTime;

public class Message implements IMessage {
    @Override
    public IUser getSender() {
        return null;
    }

    @Override
    public IUser getReciever() {
        return null;
    }

    @Override
    public LocalDateTime getTime() {
        return null;
    }

    @Override
    public String getMessageText() {
        return null;
    }
}
