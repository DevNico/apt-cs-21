package de.thro.messaging.commons.UserManager;

import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

import javax.naming.ConfigurationException;

/**
 * Zur verwaltung des Hauptusers in dem Programm
 * Der Hauptuser ist der User, der beim Programmstart als Benutzer hinterlegt werden muss.
 *
 * Bei Zusatzwünschen melde dich einfach bei mir!!!
 */

public interface IUserManager {

    /**
     * Hauptuser erstellen:
     * Erstellt einen Hauptuser, kümmert sich um das Speichern in der Config und Registriert den User in der Datenbank
     * Wenn diese Methode ausgeführt wird, dann wird der User auch gleich als Instanz im Usermanager hinterlegt und kann mit getMainUser() abgefragt werden!
     * @param name Der Name der vom User aus der Konsole abgefragt wurde.
     * @param type Der Typ der Person (Professor oder Student).
     * UserAlreadyExistsException Gibt eine Fehlermeldung zurück wenn bereits
     * ein User existiert und versucht wird einen neuen User anzulegen!
     * @throws ConfigHandlerException
     * Außerdem gibt es einen Fehler wenn irgendetwas in dem Aufruf der Config.write falsch gelaufen ist!
     */
    public void createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigHandlerException;

    /**
     * Auf vorhandenen User checken:
     * Checkt ab ob es bereits eine Config gibt, in der ein User hinterlegt wurde.
     * @return Rückgabe der Antwort true or false
     * @throws ConfigHandlerException Falls bei der Abfrage aus der Config ein Fehler passiert!
     */

    public boolean isMainUserInConfig() throws ConfigHandlerException;
    /**
     * User aus Config laden:
     * Die Methode holt sich die Userdaten aus der Config.
     * Um den Fehler zu vermeiden, kann man sich auch erst über die Methode
     * isUserInConfig() darüber vergewissern, dass es einen User gibt.
     * @return User
     * @throws ConfigHandlerException Gibt einen Konfigurationsfehler zurück etwas schiefgelaufen ist
     * @throws UserNotExistsException wenn kein User in der Config Existiert
     */
    public IUser getMainUser() throws ConfigHandlerException, UserNotExistsException;

}
