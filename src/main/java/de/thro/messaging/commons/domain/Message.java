package de.thro.messaging.commons.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    //User lässt sich nicht deserialisieren
    private final User sender;
    private final String receiver;
    private final boolean isBroadcast;
    private final String messageText;
    private final LocalDateTime dateTime;

    public Message(User sender, String receiver, boolean isBroadcast, String messageText) {
        this.sender = sender;
        this.receiver = receiver;
        this.isBroadcast = isBroadcast;
        this.messageText = messageText;
        this.dateTime = LocalDateTime.now();
    }

    public User getSender() {
        return this.sender;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public LocalDateTime getTime() {
        return this.dateTime;
    }

    public String getMessageText() {
        return this.messageText;
    }

    public boolean getIsBroadcast() {
        return this.isBroadcast;
    }


    /**
     * Formatierte Ausgabe des Timestamps, des Empfängers, des Senders und der Nachricht
     *
     * @return
     */
    @Override
    public String toString() {
        return dateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")) +
                "\n" +
                "An: " +
                (isBroadcast ? "Broadcast" : receiver) +
                "\n" +
                "Von: " +
                sender.getName() +
                " - UserTyp: " +
                sender.getUserType().toString() +
                "\n" +
                "Nachricht: " +
                messageText;
    }
}
