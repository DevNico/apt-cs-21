package de.thro.messaging.commons.UserManager;

import javax.naming.ConfigurationException;

public interface UserManager {

    /**
     * Erstellt einen Hauptuser, der der Anwender der Application ist und kümmert sich
     * um das Speichern in der Config
     * Wenn diese Methode ausgeführt wird, dann wird der User auch gleich als Instanz
     * im Usermanager hinterlegt.
     * @param name Der Name der vom User aus der Konsole Abgefragt wurde
     * @param type Der Typ der Person (Professor oder Student)
     * @throws ConfigurationException Gibt eine Fehlermeldung zurück wenn bereits
     * ein User existiert und versucht wird einen neuen User anzulegen!
     * Außerdem gibt es einen Fehler wenn irgendetwas in dem Aufruf der Config.write falsch gelaufen ist!
     */
    public void createMainUser(String name, UserType type) throws ConfigurationException;

    /**
     * Checkt ab ob es bereits eine Config gibt, in der ein User hinterlegt wurde.
     * @return Rückgabe der Antwort true or false
     */
    public boolean isUserInConfig();
    /**
     * Holt sich den User aus der Config heraus
     * Um den Fehler zu vermeiden, kann man sich auch erst über die Methode
     * createMainUser() darüber vergewissern, dass es einen User gibt.
     * @return User
     * @throws ConfigurationException Gibt einen Konfigurationsfehler zurück etwas schiefgelaufen ist
     * @throws UserNotExistsException Fehler wenn kein User in der Config Existiert
     */
    public User getMainUser() throws ConfigurationException, UserNotExistsException;




}
