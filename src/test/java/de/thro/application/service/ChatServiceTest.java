package de.thro.application.service;

import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.application.service.ChatService;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.common.DateTimeFactory;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Test
    public void itShouldSendADirectMessage() throws ApplicationException, MessageQueueConnectionException, MessageQueueConfigurationException, MessageQueueSendException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        /*
         * Next up we configure the mocks. This is phase one, where we capture (program) a behaviour
         * so we can later replay it.
         */
        final String userMail = UUID.randomUUID().toString();
        final String receiver = UUID.randomUUID().toString();
        final User user = new User(userMail, UserType.STUDENT);
        final String messageText = UUID.randomUUID().toString();

        Message message = new Message(user, receiver, false, messageText, DateTimeFactory.getDateTime());
        // We need to define the object that will be passed to the messageQueueMock.
        doNothing().when(messageQueueMock).sendDirect(message);
        // Let's create the service and execute the sendDirectMessage function
        IChatService chatService = new ChatService(messageQueueMock, user);
        chatService.sendDirectMessage(receiver, messageText);
        verify(messageQueueMock, times(1)).sendDirect(message);
        verify(messageQueueMock, times(0)).sendBroadcast(any(Message.class));
    }

    @Test
    void  itShouldGetDirectMessages() throws MessageQueueFetchException, MessageQueueConnectionException, MessageQueueConfigurationException, ApplicationException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IUserService userServiceMock = mock(IUserService.class);

        final String userName = UUID.randomUUID().toString();
        final UserType userType = UserType.TEACHER;
        final User user = new User(userName, userType);

        final String receiver = UUID.randomUUID().toString();
        final int amountMessages = 2;
        Message message;

        List<Message> directMessages = new ArrayList<>();

        for (int i = 0; i < amountMessages / 2; i++) {
            message = new Message(user,receiver, false, Integer.toString(i));
            directMessages.add(message);
        }

        List<Message> broadcastMessages = new ArrayList<>();

        for (int i = 0; i < amountMessages; i++) {
            message = new Message(user,receiver, true, Integer.toString(i));
            broadcastMessages.add(message);
        }

        when(messageQueueMock.getDirectMessages(any())).thenReturn(directMessages);
        when(messageQueueMock.getBroadcastMessages()).thenReturn(broadcastMessages);
        when(userServiceMock.getUserName()).thenReturn(user);
        when(userServiceMock.getUserType()).thenReturn(userType);

        ChatService chatService = new ChatService(messageQueueMock, userServiceMock);
        List<Message> messages = chatService.getMessages();

        verify(messageQueueMock,times(1)).getDirectMessages(userServiceMock.getUserName().toString());
        verify(messageQueueMock,times(0)).getBroadcastMessages();

        List<Message> expectedMessages = directMessages;
        expectedMessages = expectedMessages
                .stream()
                .sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());

        assertEquals(messages, expectedMessages);
    }

    @Test
    void  itShouldGetDirectAndBroadcastMessages() throws MessageQueueFetchException, MessageQueueConnectionException, MessageQueueConfigurationException, ApplicationException {
        /*  We need mocks for the following services:
         *  IMessageQueue messageQueueMock,
         *  IUserService userServiceMock
         */
        IMessageQueue messageQueueMock = mock(IMessageQueue.class);
        IUserService userServiceMock = mock(IUserService.class);

        final String userName = UUID.randomUUID().toString();
        final UserType userType = UserType.STUDENT;
        final User user = new User(userName, userType);

        final String receiver = UUID.randomUUID().toString();
        final int amountMessages = 2;

        List<Message> directMessages = new ArrayList<>();

        for (int i = 0; i < (amountMessages / 2); i++) {
            directMessages.add(new Message(user,receiver, false, Integer.toString(i)));
        }

        List<Message> broadcastMessages = new ArrayList<>();

        for (int i = 0; i < (amountMessages / 2); i++) {
            broadcastMessages.add(new Message(user,receiver, true, Integer.toString(i)));
        }

        when(messageQueueMock.getDirectMessages(any())).thenReturn(new ArrayList<>(directMessages));
        when(messageQueueMock.getBroadcastMessages()).thenReturn(new ArrayList<>(broadcastMessages));
        when(userServiceMock.getUserName()).thenReturn(user);
        when(userServiceMock.getUserType()).thenReturn(userType);

        ChatService chatService = new ChatService(messageQueueMock, userServiceMock);
        List<Message> messages = chatService.getMessages();

        verify(messageQueueMock,times(1)).getDirectMessages(userServiceMock.getUserName().toString());
        verify(messageQueueMock,times(1)).getBroadcastMessages();

        List<Message> expectedMessages = directMessages;
        expectedMessages.addAll(broadcastMessages);
        expectedMessages = expectedMessages
                .stream()
                .sorted(Comparator.comparing(Message::getTime))
                .collect(Collectors.toList());

        assertEquals(messages.size(), expectedMessages.size());
        assertEquals(messages, expectedMessages);
    }
}
