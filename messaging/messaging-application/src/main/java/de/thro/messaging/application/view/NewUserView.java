package de.thro.messaging.application.view;

import de.thro.messaging.commons.UserManager.IUserManager;
import de.thro.messaging.commons.UserManager.UserAlreadyExistsException;
import de.thro.messaging.commons.UserManager.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewUserView {

    private IUserManager um = null;
    public NewUserView(IUserManager um){
        this.um = um;
    }

    /**
     * Wenn der User noch nie angemeldet war,
     * wird ein Menü zur eingabe eine User aufgerufen.
     * Der User wird dann über den Usermanager im System erzeugt.
     */
    public void newUser(){
        System.out.println("Geben Sie einen Benutzernamen an");
        String userName = "";
        String typ = "";
        UserType userType = null;

        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

        //Nutzername von Console einlesen
        try {
            userName = obj.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //UserTyp von Console einlesen
        boolean typeIsSet = false;
        while (!typeIsSet) {
            typ = getUserType();
            if (typ.toUpperCase().equals("S")) {
                userType = UserType.STUDENT;
                typeIsSet = true;
            } else if (typ.toUpperCase().equals("P")) {
                userType = UserType.TEACHER;
                typeIsSet = true;
            } else {
                System.out.println("Das entspricht keiner Rolle. Versuchen Sie es erneut.");
            }
        }

        //lege den neuen User im System an
        try {
            um.createMainUser(userName, userType);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (ConfigHandlerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liest die Eingebe der Konsole ein und gibt den Buchstaben zurück der zur Nutzerrolle gehört.
     * @return String Buchstabe für den Usertyp
     */
    private String getUserType(){
        String typ = "";
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Geben Sie Ihre Rolle an. Professor = [P] Student = [S]");
        try {
            typ = obj.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return typ;
    }


    public IUser loadUser(){
        try {
            return um.getMainUser();
        } catch (ConfigHandlerException |  UserNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

}
