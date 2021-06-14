package de.thro.messaging.viewcontroller;

import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConfigurationException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueFetchException;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueSendException;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.application.service.IUserService;
import de.thro.messaging.commons.usermanager.UserManager;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;

import java.util.LinkedList;
import java.util.List;

/**
 * Dieser Controller kümmert sich um die Verbindung von des user interface und der Implementierung.
 * Alle Ein- und Ausgaben der View werden hier verwaltet.
 *
 * @author patricklanzinger
 */
public class ViewController {

    private IUserService userService;
    private IChatService chatService;

    /**
     * Beendet die Anwendung.
     */
    public void endApp() {
        System.exit(0);
    }

    /**
     * Beim Erstellen des Kontrollers soll direkt die konkrete Implementierung für den UserManger und die
     * Netzwerkanbindung übergeben werden.
     *
     * @param userManager Verwaltung der User.
     * @param messaging   Verwaltung der Empfangenen und Gesendeten Nachrichten.
     */
    public ViewController(IUserService userService, IChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    /**
     * Erstellen einer Direktnachricht.
     *
     * @param reciever    Empfänger als String.
     * @param messageText Text der Nachricht.
     * @return Gibt die Fertige Nachricht zurück.
     */
    public Message createDirektMessage(String reciever, String messageText) {
        try {
            return new Message(userManager.getMainUser(), reciever, false, messageText);
        } catch (ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }

        return new Message(null, null, false, null);
    }

    /**
     * Erstellen einer Rundnachricht. Hier muss nur der Text angegeben werden. Empfänger ist nicht notwendig, da an
     * alle gesendet wird.
     *
     * @param messageText Text der Nachricht.
     * @return Gibt die fertige Nachricht zurück.
     */
    public Message createBroadcastMessage(String messageText) {
        try {
            return new Message(userManager.getMainUser(), null, true, messageText);
        } catch (ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }

        return new Message(null, null, false, null);
    }

    /**
     * Direktnachricht versenden.
     *
     * @param message Nachricht die versendet werden soll.
     * @return Versenden erfolgreich?
     */
    public boolean sendDirect(Message message) {
        try {
            messaging.sendDirect(message);
            return true;
        } catch (MessageQueueConnectionException | MessageQueueSendException | MessageQueueConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Rundnachricht versenden.
     *
     * @param message Nachricht die versendet werden soll.
     * @return Versenden erfolgreich?
     */
    public boolean sendBroadcast(Message message) {
        try {
            messaging.sendBroadcast(message);
            return true;
        } catch (MessageQueueConnectionException | MessageQueueSendException | MessageQueueConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gibt alle Empfangen Nachrichten als Liste von String[] aus. Index 1 ist Sender, 2 ist Zeit und 3
     * ist der Nachrichtentext.
     *
     * @return List<String [ ]>
     */
    public List<Message> displayReceivedMessages() {
        List<Message> messages = new LinkedList<>();
        try {
            messages = messaging.receiveAll();
        } catch (MessageQueueConnectionException | MessageQueueFetchException | MessageQueueConfigurationException e) {
            e.printStackTrace();
        }
        return messages;
    }


}
