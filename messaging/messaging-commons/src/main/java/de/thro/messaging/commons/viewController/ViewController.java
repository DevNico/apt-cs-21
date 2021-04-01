package de.thro.messaging.commons.viewController;

import de.thro.messaging.commons.UserManager.IUserManager;
import de.thro.messaging.commons.UserManager.UserAlreadyExistsException;
import de.thro.messaging.commons.UserManager.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;
import de.thro.messaging.commons.domain.impl.Message;
import de.thro.messaging.commons.network.IMessaging;
import de.thro.messaging.commons.network.NetworkException;

import java.util.LinkedList;
import java.util.List;

/**
 * Dieser Controller kümmert sich um die Verbindung von des user interface und der Implementierung.
 * Alle Ein- und Ausgaben der View werden hier verwaltet.
 * @author patricklanzinger
 */
public class ViewController {

    IUserManager userManager;
    IMessaging messaging;

    /**
     * Beim Erstellen des Kontrollers soll direkt die konkrete Implementierung für den UserManger und die
     * Netzwerkanbindung übergeben werden.
     * @param userManager Verwaltung der User.
     * @param messaging Verwaltung der Empfangenen und Gesendeten Nachrichten.
     */
    public ViewController(IUserManager userManager, IMessaging messaging){
        this.userManager = userManager;
        this.messaging = messaging;
    }

    public IUser loadUser(){
        try {
            return userManager.getMainUser();
        } catch (ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createUser(String name, UserType userType) {
        try {
            userManager.createMainUser(name, userType);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (ConfigHandlerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellen einer Direktnachricht.
     * @param reciever Empfänger als String.
     * @param messageText Text der Nachricht.
     * @return Gibt die Fertige Nachricht zurück.
     */
    public IMessage createDirektMessage(String reciever, String messageText){
        try {
            IMessage message = new Message(userManager.getMainUser(), reciever, false, messageText);
            return  message;
        } catch (ConfigHandlerException e) {
            e.printStackTrace();

        } catch (UserNotExistsException e) {
            e.printStackTrace();
        }
        return new Message(null, null, false, null);
    }

    /**
     * Erstellen einer Rundnachricht. Hier muss nur der Text angegeben werden. Empfänger ist nicht notwendig, da an
     * alle gesendet wird.
     * @param messageText Text der Nachricht.
     * @return Gibt die fertige Nachricht zurück.
     */
    public IMessage createBroadcastMessage( String messageText){
        try {
            IMessage message = new Message(userManager.getMainUser(), null, true, messageText);
            return  message;
        } catch (ConfigHandlerException e) {
            e.printStackTrace();

        } catch (UserNotExistsException e) {
            e.printStackTrace();
        }
        return new Message(null, null, false, null);
    }

    public boolean sendDirect (IMessage message){
        try {
            messaging.sendDirect(message);
            return true;
        } catch (NetworkException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendBroadcast(IMessage message){
        try {
            messaging.sendBroadcast(message);
            return true;
        } catch (NetworkException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String []> displayReceivedMessages(){
        List<IMessage> messages = new LinkedList<>();
        try {
            messages = messaging.receiveAll();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        List<String []> ouput = new LinkedList<>();
        for (IMessage m: messages) {
            ouput.add(new String[]{m.getSender().getName(), m.getTime().toString(), m.getMessageText()});
        }
        return ouput;
    }



}
