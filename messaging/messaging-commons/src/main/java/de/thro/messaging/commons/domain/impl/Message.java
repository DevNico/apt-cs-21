package de.thro.messaging.commons.domain.impl;

import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements IMessage {

    //IUser lässt sich nicht deserialisieren
    private User sender;
    private String reciever;
    private boolean isBroadcast;
    private String messageText;
    private LocalDateTime dateTime;

    public Message (User sender, String reciever, boolean isBroadcast, String messageText){
        this.sender = sender;
        this.reciever = reciever;
        this.isBroadcast = isBroadcast;
        this.messageText = messageText;
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public IUser getSender() {
        return this.sender;
    }

    @Override
    public String getReciever() {
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

    @Override
    public boolean getIsBrodcast() {
        return this.isBroadcast;
    }


    /**
     * Formatierte Ausgabe des Timestamps, des Empfängers, des Senders und der Nachricht
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")))
                .append("\n")
                .append("An: ")
                .append(isBroadcast ? "Broadcast" : receiver)
                .append("\n")
                .append("Von: ")
                .append(sender.getName())
                .append(" - UserTyp: ")
                .append(sender.getUserType().toString())
                .append("\n")
                .append("Nachricht: ")
                .append(messageText);

        return stringBuilder.toString();
    }
}
