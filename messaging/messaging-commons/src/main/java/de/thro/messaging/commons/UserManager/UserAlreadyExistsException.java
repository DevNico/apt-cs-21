package de.thro.messaging.commons.UserManager;

/**
 * Fehler wenn ein Hauptbenutzer schon existiert!!
 * @author Franz Murner
 */
public class UserAlreadyExistsException extends Exception{


    public UserAlreadyExistsException(String msg){
        super(msg);
    }

    public UserAlreadyExistsException(){
        super();
    }


}
