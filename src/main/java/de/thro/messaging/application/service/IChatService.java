package de.thro.messaging.application.service;

import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.domain.models.Message;

import java.util.List;

/**
 * Darf von presentation genutzt werden.
 */
public interface IChatService {
    /**
     *
     * @param receiver
     * @param message
     */
    void sendDirectMessage(String receiver, String message) throws ApplicationException;

    /**
     *
     * @param message
     */
    void sendBroadCast(String message) throws ApplicationException;

    /**
     *
     * @return
     */
    List<Message> getMessages() throws ApplicationException;

}
