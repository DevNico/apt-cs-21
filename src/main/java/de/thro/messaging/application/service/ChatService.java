package de.thro.messaging.application.service;

import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.common.DateTimeFactory;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChatService implements IChatService {

    public static final int MAX_FETCH_TRIES = 5;

    private final IMessageQueue messageQueue;
    private final User user;
    private int sendTries = 0;

    public ChatService(IMessageQueue messageQueue, User user) {
        this.messageQueue = messageQueue;
        this.user = user;
    }

    @Override
    public void sendDirectMessage(String receiver, String message) throws ApplicationException {
        Message message1 = new Message(user, receiver, false, message, DateTimeFactory.getDateTime());
        try {
            this.messageQueue.sendDirect(message1);
        } catch (MessageQueueConnectionException e) {
            e.printStackTrace();
        } catch (MessageQueueSendException e) {
            throw new ApplicationException("Direktnachricht konnte nicht versendet werden", e);
        } catch (MessageQueueConfigurationException e) {
            throw new ApplicationException("MessageQueue wurde falsch konfiguriert", e);
        }
    }

    @Override
    public void sendBroadCast(String message) throws ApplicationException {
        Message message1 = new Message(user, null, true, message, DateTimeFactory.getDateTime());
        try {
            this.messageQueue.sendBroadcast(message1);
        } catch (MessageQueueConnectionException e) {
            retryFetchMessages();
        } catch (MessageQueueSendException e) {
            throw new ApplicationException("Broadcast Nachricht konnte nicht versendet werden", e);
        } catch (MessageQueueConfigurationException e) {
            throw new ApplicationException("Broadcast Queue wurde falsch konfiguriert", e);
        }
    }

    @Override
    public List<Message> getMessages() throws ApplicationException {
        try {
            List<Message> messages = this.messageQueue.getDirectMessages(this.user.getName());
            if (user.getUserType().equals(UserType.STUDENT)) {
                messages.addAll(this.messageQueue.getBroadcastMessages());
            }
            return messages
                    .stream()
                    .sorted(Comparator.comparing(Message::getTime))
                    .collect(Collectors.toList());
        } catch (MessageQueueFetchException | MessageQueueConnectionException e) {
            return retryFetchMessages();
        } catch (MessageQueueConfigurationException e) {
            throw new ApplicationException("Chatsystem falsch konfiguriert", e);
        }
    }

    private List<Message> retryFetchMessages() throws ApplicationException {
        sendTries++;
        if (sendTries <= MAX_FETCH_TRIES) {
            throw new ApplicationException("Chatserver kann nicht erreicht werden");
        }
        try {
            Thread.currentThread().wait(100);
            return getMessages();
        } catch (InterruptedException ie) {
            throw new ApplicationException("Interrupt reveived...", ie);
        }
    }

}