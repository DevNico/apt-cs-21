package de.thro.application.service;

import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.application.exceptions.UserAlreadyExistsException;
import de.thro.messaging.application.service.ChatService;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.application.service.IUserManager;
import de.thro.messaging.common.DateTimeFactory;
import de.thro.messaging.common.IDateTimeFactory;
import de.thro.messaging.common.confighandler.ConfigHandlerException;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Test
    void itShouldSendADirectMessage() throws ApplicationException, MessageQueueConnectionException, MessageQueueConfigurationException, MessageQueueSendException, UserNotExistsException, ConfigHandlerException, UserAlreadyExistsException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IDateTimeFactory dateTimeFactory = mock(IDateTimeFactory.class);
        /*
         * Next up we configure the mocks. This is phase one, where we capture (program) a behaviour
         * so we can later replay it.
         */
        final String userName = UUID.randomUUID().toString();
        final String receiver = UUID.randomUUID().toString();
        final UserType userType = UserType.STUDENT;
        final User user = new User(userName, userType);
        final String messageText = UUID.randomUUID().toString();
        final var localDateTime = LocalDateTime.now();

        when(dateTimeFactory.getDateTime()).thenReturn(localDateTime);
        Message message = new Message(user, receiver, false, messageText, dateTimeFactory.getDateTime());
        // We need to define the object that will be passed to the messageQueueMock.
        doNothing().when(messageQueueMock).sendDirect(message);
        // Let's create the service and execute the sendDirectMessage function
        IChatService chatService = new ChatService(messageQueueMock, user, dateTimeFactory);
        chatService.sendDirectMessage(receiver, messageText);
        verify(messageQueueMock, times(1)).sendDirect(message);
        verify(messageQueueMock, times(0)).sendBroadcast(any(Message.class));
    }

        @Test
    void itShouldGetDirectMessages() throws MessageQueueFetchException, MessageQueueConnectionException, MessageQueueConfigurationException, ApplicationException, ConfigHandlerException, UserNotExistsException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IDateTimeFactory dateTimeFactory = mock(IDateTimeFactory.class);

        final String userName = UUID.randomUUID().toString();
        final UserType userType = UserType.TEACHER;
        final User user = new User(userName, userType);
        final var localDateTime = LocalDateTime.now();

        final String receiver = UUID.randomUUID().toString();
        final int amountMessages = 2;
        Message message;

        List<Message> directMessages = new ArrayList<>();

        for (int i = 0; i < amountMessages / 2; i++) {
            message = new Message(user, receiver, false, Integer.toString(i), localDateTime);
            directMessages.add(message);
        }

        List<Message> broadcastMessages = new ArrayList<>();

        for (int i = 0; i < amountMessages; i++) {
            message = new Message(user, receiver, true, Integer.toString(i), localDateTime);
            broadcastMessages.add(message);
        }

        when(dateTimeFactory.getDateTime()).thenReturn(localDateTime);
        when(messageQueueMock.getDirectMessages(any())).thenReturn(directMessages);
        when(messageQueueMock.getBroadcastMessages()).thenReturn(broadcastMessages);

        ChatService chatService = new ChatService(messageQueueMock, user, dateTimeFactory);
        List<Message> messages = chatService.getMessages();

        verify(messageQueueMock, times(1)).getDirectMessages(user.getName());
        verify(messageQueueMock, times(0)).getBroadcastMessages();

        List<Message> expectedMessages = directMessages;
        expectedMessages = expectedMessages
                .stream()
                .sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());

        assertEquals(messages, expectedMessages);
    }

    @Test
    void itShouldGetDirectAndBroadcastMessages() throws MessageQueueFetchException, MessageQueueConnectionException, MessageQueueConfigurationException, ApplicationException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IDateTimeFactory dateTimeFactory = mock(IDateTimeFactory.class);

        final String userName = UUID.randomUUID().toString();
        final UserType userType = UserType.STUDENT;
        final User user = new User(userName, userType);
        final var localDateTime = LocalDateTime.now();

        final String receiver = UUID.randomUUID().toString();
        final int amountMessages = 2;

        List<Message> directMessages = new ArrayList<>();

        for (int i = 0; i < (amountMessages / 2); i++) {
            directMessages.add(new Message(user, receiver, false, Integer.toString(i), localDateTime));
        }

        List<Message> broadcastMessages = new ArrayList<>();

        for (int i = 0; i < (amountMessages / 2); i++) {
            broadcastMessages.add(new Message(user, receiver, true, Integer.toString(i), localDateTime));
        }

        when(dateTimeFactory.getDateTime()).thenReturn(localDateTime);
        when(messageQueueMock.getDirectMessages(any())).thenReturn(new ArrayList<>(directMessages));
        when(messageQueueMock.getBroadcastMessages()).thenReturn(new ArrayList<>(broadcastMessages));

        ChatService chatService = new ChatService(messageQueueMock, user, dateTimeFactory);
        List<Message> messages = chatService.getMessages();

        verify(messageQueueMock, times(1)).getDirectMessages(user.getName());
        verify(messageQueueMock, times(1)).getBroadcastMessages();

        List<Message> expectedMessages = directMessages;
        expectedMessages.addAll(broadcastMessages);
        expectedMessages = expectedMessages
                .stream()
                .sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());

        assertEquals(messages.size(), expectedMessages.size());
        assertEquals(messages, expectedMessages);
    }

    @Test
    void itShouldSendABroadCastMessage() throws ApplicationException, MessageQueueConnectionException, MessageQueueConfigurationException, MessageQueueSendException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IDateTimeFactory dateTimeFactory = mock(IDateTimeFactory.class);
        /*
         * Next up we configure the mocks. This is phase one, where we capture (program) a behaviour
         * so we can later replay it.
         */
        final String userMail = UUID.randomUUID().toString();
        final User user = new User(userMail, UserType.STUDENT);
        final String messageText = UUID.randomUUID().toString();
        final var localDateTime = LocalDateTime.now();

        when(dateTimeFactory.getDateTime()).thenReturn(localDateTime);

        Message message = new Message(user,null, true, messageText, dateTimeFactory.getDateTime());
        // We need to define the object that will be passed to the messageQueueMock.
        doNothing().when(messageQueueMock).sendBroadcast(message);
        // Let's create the service and execute the sendDirectMessage function
        IChatService chatService = new ChatService(messageQueueMock, user,dateTimeFactory);
        chatService.sendBroadCast(messageText);
        verify(messageQueueMock, times(1)).sendBroadcast(message);
        verify(messageQueueMock,times(0)).sendDirect(any(Message.class));
    }
}
