package de.thro.messaging.commons.UserManager;

/**
 * Fehler wenn noch kein Hauptbenutzer existiert!
 * @author Franz Murner
 */
public class UserNotExistsException extends Exception{


    public UserNotExistsException(String msg){
        super(msg);
    }

    public UserNotExistsException(){
        super();
    }


}
