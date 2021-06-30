package de.thro.messaging.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Message.
 */
public class Message {
    private final User sender;
    private final String receiver;
    private final boolean isBroadcast;
    private final String messageText;
    private final LocalDateTime dateTime;

    /**
     * Instantiates a new Message.
     *
     * @param sender      the sender
     * @param receiver    the receiver
     * @param isBroadcast the is broadcast
     * @param messageText the message text
     */
    public Message(User sender, String receiver, boolean isBroadcast, String messageText, LocalDateTime dateTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.isBroadcast = isBroadcast;
        this.messageText = messageText;
        this.dateTime = dateTime;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public User getSender() {
        return this.sender;
    }

    /**
     * Gets receiver.
     *
     * @return the receiver
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public LocalDateTime getTime() {
        return this.dateTime;
    }

    /**
     * Gets message text.
     *
     * @return the message text
     */
    public String getMessageText() {
        return this.messageText;
    }

    /**
     * Gets is broadcast.
     *
     * @return the is broadcast
     */
    public boolean getIsBroadcast() {
        return this.isBroadcast;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + getSender() +
                ", receiver='" + getReceiver() + '\'' +
                ", isBroadcast=" + getIsBroadcast() +
                ", messageText='" + getMessageText() + '\'' +
                ", dateTime=" + getTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var message = (Message) o;
        return isBroadcast == message.isBroadcast && Objects.equals(getSender(), message.getSender()) && Objects.equals(getReceiver(), message.getReceiver()) && Objects.equals(getMessageText(), message.getMessageText()) && Objects.equals(dateTime, message.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSender(), getReceiver(), isBroadcast, getMessageText(), dateTime);
    }
}
