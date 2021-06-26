package de.thro.messaging.application.dependencies.messagequeue;

import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.infrastructure.messagequeue.ActiveMQ;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IMessageQueueTest {

    private IMessageQueue getMessageQueue(User user) throws MessageQueueConnectionException {
        return new ActiveMQ(new MessageQueueConfiguration("localhost", "61616", "admin", "admin"), user);
    }

    @Test
    void whenBroadcastMessageSent_getBroadcastMessagesShouldContain() throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException, MessageQueueFetchException, InterruptedException {
        final var user = new User("Peter", UserType.TEACHER);
        final var messageQueue = getMessageQueue(user);
        final var message = new Message(user, "", true, "Hello there", LocalDateTime.now());

        messageQueue.sendBroadcast(message);

        await().atMost(5, TimeUnit.SECONDS).until(broadcastMessageReceived(messageQueue, message));

        final var broadcastMessages = messageQueue.getBroadcastMessages();
        assertEquals(1, broadcastMessages.size());
        assertEquals(message, broadcastMessages.get(0));
    }

    private Callable<Boolean> broadcastMessageReceived(IMessageQueue messageQueue, Message message) {
        return () -> {
            final var broadcastMessages = messageQueue.getBroadcastMessages();
            return broadcastMessages.size() == 1 && broadcastMessages.get(0).equals(message);
        };
    }

    @Test
    void whenDirectMessageSent_getDirectMessagesShouldContain() throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException, MessageQueueFetchException, InterruptedException {
        final var userSender = new User("Peter", UserType.TEACHER);
        final var userReceiver = new User("Peter", UserType.TEACHER);
        final var messageQueue = getMessageQueue(userReceiver);
        final var message = new Message(userSender, userReceiver.getName(), false, "Hello there", LocalDateTime.now());

        messageQueue.sendDirect(message);

        await().atMost(5, TimeUnit.SECONDS).until(directMessageReceived(messageQueue, message));

        final var directMessages = messageQueue.getDirectMessages("");
        assertEquals(1, directMessages.size());
        assertEquals(message, directMessages.get(0));
    }

    private Callable<Boolean> directMessageReceived(IMessageQueue messageQueue, Message message) {
        return () -> {
            final var directMessages = messageQueue.getDirectMessages("");
            return directMessages.size() == 1 && directMessages.get(0).equals(message);
        };
    }
}