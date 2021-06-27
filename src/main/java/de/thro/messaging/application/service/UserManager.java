package de.thro.messaging.application.service;

import com.google.gson.Gson;
import de.thro.messaging.application.exceptions.UserAlreadyExistsException;
import de.thro.messaging.common.confighandler.ConfigHandler;
import de.thro.messaging.common.confighandler.ConfigHandlerException;
import de.thro.messaging.common.confighandler.ConfigUser;
import de.thro.messaging.common.confighandler.IConfigHandler;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.common.serialization.ISerializer;
import de.thro.messaging.common.serialization.SerializerJson;

/**
 * Diese Klasse implementiert das Interface UserManager
 *
 * @author Franz Murner
 */
public class UserManager implements IUserManager {
    ISerializer<ConfigUser> serializer = new SerializerJson<>(ConfigUser.class, new Gson());

    public UserManager(ISerializer<ConfigUser> serializer) {
        this.serializer = serializer;
    }

    private User user;

    /**
     * Anlegen einer ConfighandlerInstanz zum aufruf der Daten aus der UserConfig
     */
    IConfigHandler<ConfigUser> configHandler = new ConfigHandler<>(serializer);

    /**
     * @param name Der Name der vom User aus der Konsole abgefragt wurde.
     * @param type Der Typ der Person (Professor oder Student).
     *             UserAlreadyExistsException Gibt eine Fehlermeldung zurück wenn bereits
     *             ein User existiert und versucht wird einen neuen User anzulegen!
     * @throws UserAlreadyExistsException
     * @throws ConfigHandlerException
     * @return
     */
    @Override
    public User createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigHandlerException {
        user = new User(name, type);
        createConfigFromUser(user);
        return user;
    }

    /**
     * Abfrage des Config Handlers ob File vorhanden ist.
     *
     * @return Wenn eine Datei vorhanden und der Inhalt sinn ergibt dann true ansonsten false
     * @throws ConfigHandlerException
     */
    @Override
    public boolean isMainUserInConfig() throws ConfigHandlerException {

        if (!configHandler.isFileAvailable()) return false;

        var configUser = configHandler.readConfig(null);

        return configUser != null;
    }

    /**
     * Abfrage ob ein UserConfig bereits existiert über die isMainUserInConfig() ansonsten Fehler
     * Wenn User bereits in Klasse existiert dann Instanz merken, ansonsten einen neuen mit der Basis der Config anlegen
     *
     * @return User
     * @throws UserNotExistsException wird geworfen, wenn noch kein User in der Config existiert.
     * @throws ConfigHandlerException wird geworfen, wenn etwas im ConfigHandler nicht Funktioniert hat...
     */
    @Override
    public User getMainUser() throws ConfigHandlerException, UserNotExistsException {
        if (!isMainUserInConfig())
            throw new UserNotExistsException("Fehler: Es wurde noch kein Hauptbenutzer angelegt!");

        if (user == null) {
            var configUser = configHandler.readConfig();
            user = createUserFromConfig(configUser);
        }
        return user;
    }

    private User createUserFromConfig(ConfigUser configUser) {
        return new User(configUser.getName(), configUser.getType());
    }

    private void createConfigFromUser(User user) throws ConfigHandlerException, UserAlreadyExistsException {
        if (isMainUserInConfig())
            throw new UserAlreadyExistsException("Es existiert bereits ein Hauptbenutzer in der Config");

        var configUser = new ConfigUser(user.getName(), user.getUserType());
        configHandler.writeConfig(configUser);
    }
}
