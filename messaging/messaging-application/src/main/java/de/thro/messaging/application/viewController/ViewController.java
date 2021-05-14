package de.thro.messaging.application.viewController;

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
     * Beendet die Anwendung.
     */
    public void endApp(){
        System.exit(0);
    }

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

    /**
     * Direktnachricht versenden.
     * @param message Nachricht die versendet werden soll.
     * @return Versenden erfolgreich?
     */
    public boolean sendDirect (IMessage message){
        try {
            messaging.sendDirect(message);
            return true;
        } catch (NetworkException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Rundnachricht versenden.
     * @param message Nachricht die versendet werden soll.
     * @return Versenden erfolgreich?
     */
    public boolean sendBroadcast(IMessage message){
        try {
            messaging.sendBroadcast(message);
            return true;
        } catch (NetworkException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gibt alle Empfangen Nachrichten als Liste von String[] aus. Index 1 ist Sender, 2 ist Zeit und 3
     * ist der Nachrichtentext.
     * @return List<String[]>
     */
    public List<IMessage> displayReceivedMessages(){
        List<IMessage> messages = new LinkedList<>();
        try {
            messages = messaging.receiveAll();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return messages;
    }



}