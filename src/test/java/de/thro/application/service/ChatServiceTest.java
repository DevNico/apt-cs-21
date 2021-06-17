package de.thro.application.service;

import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.application.service.ChatService;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.common.DateTimeFactory;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
}
