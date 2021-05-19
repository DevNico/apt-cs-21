package de.thro.messaging.application.view;

import de.thro.messaging.commons.usermanager.UserManager;
import de.thro.messaging.commons.usermanager.UserAlreadyExistsException;
import de.thro.messaging.commons.usermanager.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.domain.User;
import de.thro.messaging.commons.domain.UserType;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class NewUserView {

    private UserManager um;

    public NewUserView(UserManager um) {
        this.um = um;
    }

    /**
     * Wenn der User noch nie angemeldet war,
     * wird ein Menü zur eingabe eine User aufgerufen.
     * Der User wird dann über den Usermanager im System erzeugt.
     */
    public void newUser() {
        System.out.println("Geben Sie einen Benutzernamen an");
        var userName = "";
        UserType userType = null;

        try (final var scanner = new Scanner(System.in)) {
            // Nutzername von Console einlesen
            userName = scanner.next();

            // UserTyp von Console einlesen
            while (userType == null) {
                System.out.println("Geben Sie Ihre Rolle an. Professor = [P] Student = [S]");
                final var input = scanner.next().toLowerCase();

                switch (input) {
                    case "s":
                        userType = UserType.STUDENT;
                        break;
                    case "p":
                        userType = UserType.TEACHER;
                        break;
                    default:
                        System.out.println("Das entspricht keiner Rolle. Versuchen Sie es erneut.");
                        break;
                }
            }

            um.createMainUser(userName, userType);
        } catch (NoSuchElementException | UserAlreadyExistsException | ConfigHandlerException e) {
            e.printStackTrace();
        }
    }

    public User loadUser() {
        try {
            return um.getMainUser();
        } catch (ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

}
