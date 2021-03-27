package de.thro.messaging.commons.domain.impl;

import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;

import java.time.LocalDateTime;

public class Message implements IMessage {

    private IUser sender;
    private IUser reciever;
    private String messageText;
    private LocalDateTime dateTime;

    public Message (IUser sender, IUser reciever, String messageText){
        this.sender = sender;
        this.reciever = reciever;
        this.messageText = messageText;
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public IUser getSender() {
        return this.sender;
    }

    @Override
    public IUser getReciever() {
        return this.reciever;
    }

    @Override
    public LocalDateTime getTime() {
        return this.dateTime;
    }

    @Override
    public String getMessageText() {
        return this.messageText;
    }
}
