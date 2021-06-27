package de.thro.messaging.infrastructure.messagequeue;

import com.google.gson.GsonBuilder;
import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.MessageQueueConfiguration;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;
import java.util.LinkedList;
import java.util.List;

public class ActiveMQ implements IMessageQueue {
    private static final Logger LOGGER = LogManager.getLogger(ActiveMQ.class);
    private static final String BROADCAST_TOPIC = "broadcast";

    private final Connection connection;
    private final Session session;

    private final MessageProducer broadcastProducer;

    private final User user;
    private final List<Message> directMessages;
    private final List<Message> broadcastMessages;

    public ActiveMQ(MessageQueueConfiguration configuration, User user) throws MessageQueueConnectionException {
        this.user = user;
        directMessages = new LinkedList<>();
        broadcastMessages = new LinkedList<>();

        final var factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(String.format("tcp://%s:%s", configuration.getIp(), configuration.getPort()));
        factory.setUserName(configuration.getUsername());
        factory.setPassword(configuration.getPassword());

        try {
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            final var directDestination = session.createTopic(user.getName());
            final var directConsumer = session.createConsumer(directDestination);
            directConsumer.setMessageListener(message -> messageListener(directMessages, message));

            final var broadcastDestination = session.createTopic(BROADCAST_TOPIC);
            broadcastProducer = session.createProducer(broadcastDestination);
            final var broadcastConsumer = session.createConsumer(broadcastDestination);
            broadcastConsumer.setMessageListener(message -> messageListener(broadcastMessages, message));
        } catch (JMSException e) {
            throw new MessageQueueConnectionException("Error connecting to ActiveMQ", e);
        }
    }

    @Override
    public void sendDirect(Message message) throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException {
        final var textMessage = toTextMessage(message);

        try {
            final var destination = session.createTopic(message.getReceiver());
            final var producer = session.createProducer(destination);
            producer.send(textMessage);
        } catch (JMSException e) {
            throw new MessageQueueSendException(String.format("Couldn't send message to %s", message.getReceiver()), e);
        }
    }

    @Override
    public void sendBroadcast(Message message) throws MessageQueueConnectionException, MessageQueueSendException, MessageQueueConfigurationException {
        final var textMessage = toTextMessage(message);

        try {
            broadcastProducer.send(textMessage);
        } catch (JMSException e) {
            throw new MessageQueueSendException("Couldn't send Message to broadcast channel", e);
        }
    }

    @Override
    public List<Message> getDirectMessages(String userName) throws MessageQueueConnectionException, MessageQueueFetchException, MessageQueueConfigurationException {
        return directMessages;
    }

    @Override
    public List<Message> getBroadcastMessages() {
        return broadcastMessages;
    }

    private void messageListener(List<Message> list, javax.jms.Message message) {
        if (message instanceof TextMessage) {
            try {
                list.add(fromTextMessage((TextMessage) message));
            } catch (MessageQueueFetchException e) {
                LOGGER.error("An error occurred while fetching the message queue.",e);
            }
        }
    }

    private TextMessage toTextMessage(Message message) throws MessageQueueSendException {
        final var gson = new GsonBuilder().create();
        try {
            return session.createTextMessage(gson.toJson(message));
        } catch (JMSException e) {
            throw new MessageQueueSendException("Couldn't create Message.", e);
        }
    }

    private Message fromTextMessage(TextMessage message) throws MessageQueueFetchException {
        final var gson = new GsonBuilder().create();

        try {
            return gson.fromJson(message.getText(), Message.class);
        } catch (JMSException e) {
            throw new MessageQueueFetchException("Couldn't resolve Message.", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
