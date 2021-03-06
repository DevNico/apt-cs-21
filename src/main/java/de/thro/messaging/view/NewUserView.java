package de.thro.messaging.view;

import de.thro.messaging.application.service.UserManager;
import de.thro.messaging.application.exceptions.UserAlreadyExistsException;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.common.confighandler.ConfigHandlerException;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.domain.enums.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class NewUserView {
    private static final Logger LOGGER = LogManager.getLogger(NewUserView.class);

    private UserManager um;

    public NewUserView(UserManager um) {
        this.um = um;
    }

    /**
     * Wenn der User noch nie angemeldet war,
     * wird ein Menü zur eingabe eine User aufgerufen.
     * Der User wird dann über den Usermanager im System erzeugt.
     * @return
     */
    public void newUser() {
        LOGGER.info("Geben Sie einen Benutzernamen an");
        var userName = "";
        UserType userType = null;

        try (final var scanner = new Scanner(System.in)) {
            // Nutzername von Console einlesen
            userName = scanner.next();

            // UserTyp von Console einlesen
            while (userType == null) {
                LOGGER.info("Geben Sie Ihre Rolle an. Professor = [P] Student = [S]");
                final var input = scanner.next().toLowerCase();

                switch (input) {
                    case "s":
                        userType = UserType.STUDENT;
                        break;
                    case "p":
                        userType = UserType.TEACHER;
                        break;
                    default:
                        LOGGER.error("Das entspricht keiner Rolle. Versuchen Sie es erneut.");
                        break;
                }
            }

             um.createMainUser(userName, userType);
        } catch (NoSuchElementException | UserAlreadyExistsException | ConfigHandlerException e) {
            LOGGER.error("An error occurred while creating the user.", e);
        }
    }

    public User loadUser() {
        try {
            return um.getMainUser();
        } catch (ConfigHandlerException | UserNotExistsException e) {
            LOGGER.error("An error occurred while loading the user.", e);
        }
        return null;
    }

}
