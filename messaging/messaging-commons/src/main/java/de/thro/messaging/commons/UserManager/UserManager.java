package de.thro.messaging.commons.UserManager;

import javax.naming.ConfigurationException;

/**
 * Zur verwaltung des Hauptusers in dem Programm
 * Der Hauptuser ist der User, der beim Programmstart als Benutzer hinterlegt werden muss.
 *
 * Bei Zusatzwünschen melde dich einfach bei mir!!!
 */

public interface UserManager {

    /**
     * Hauptuser erstellen:
     * Erstellt einen Hauptuser, kümmert sich um das Speichern in der Config und Registriert den User in der Datenbank
     * Wenn diese Methode ausgeführt wird, dann wird der User auch gleich als Instanz im Usermanager hinterlegt und kann mit getMainUser() abgefragt werden!
     * @param name Der Name der vom User aus der Konsole abgefragt wurde.
     * @param type Der Typ der Person (Professor oder Student).
     * @throws UserAlreadyExistsException Gibt eine Fehlermeldung zurück wenn bereits
     * ein User existiert und versucht wird einen neuen User anzulegen!
     * @throws ConfigurationException
     * Außerdem gibt es einen Fehler wenn irgendetwas in dem Aufruf der Config.write falsch gelaufen ist!
     */
    public void createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigurationException;

    /**
     * Auf vorhandenen User checken:
     * Checkt ab ob es bereits eine Config gibt, in der ein User hinterlegt wurde.
     * @return Rückgabe der Antwort true or false
     * @throws ConfigurationException Falls bei der Abfrage aus der Config ein Fehler passiert!
     */

    public boolean isUserInConfig() throws ConfigurationException;
    /**
     * User aus Config laden:
     * Die Methode holt sich die Userdaten aus der Config.
     * Um den Fehler zu vermeiden, kann man sich auch erst über die Methode
     * isUserInConfig() darüber vergewissern, dass es einen User gibt.
     * @return User
     * @throws ConfigurationException Gibt einen Konfigurationsfehler zurück etwas schiefgelaufen ist
     * @throws UserNotExistsException Fehler wenn kein User in der Config Existiert
     */
    public User getMainUser() throws ConfigurationException, UserNotExistsException;

}
