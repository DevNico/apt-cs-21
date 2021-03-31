package de.thro.messaging.commons.UserManager;

import de.thro.messaging.commons.confighandler.ConfigHandler;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.confighandler.ConfigUser;
import de.thro.messaging.commons.confighandler.IConfigHandler;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;
import de.thro.messaging.commons.domain.impl.User;
import de.thro.messaging.commons.serialization.ISerializer;
import de.thro.messaging.commons.serialization.SerializerJson;

/**
 * Diese Klasse implementiert das Interface IUserManager
 * @author Franz Murner
 */
public class UserManager implements IUserManager {

    private static IUser user;

    //TODO: Hinzufügen eines Serializers zum ConfigHandler
    /**
     * Anlegen einer ConfighandlerInstanz zum aufruf der Daten aus der UserConfig
     */
    IConfigHandler<ConfigUser> configHandler = new ConfigHandler<>(null);

    /**
     *
     * @param name Der Name der vom User aus der Konsole abgefragt wurde.
     * @param type Der Typ der Person (Professor oder Student).
     * UserAlreadyExistsException Gibt eine Fehlermeldung zurück wenn bereits
     * ein User existiert und versucht wird einen neuen User anzulegen!
     * @throws UserAlreadyExistsException
     * @throws ConfigHandlerException
     */
    @Override
    public void createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigHandlerException {
        user = new User(name, type);
        createConfigFromUser(user);
    }

    /**
     * Abfrage des Config Handlers ob File vorhanden ist.
     * @return Wenn eine Datei vorhanden und der Inhalt sinn ergibt dann true ansonsten false
     * @throws ConfigHandlerException
     */
    @Override
    public boolean isMainUserInConfig() throws ConfigHandlerException {

        if(!configHandler.isFileAvailable() return false;

        ConfigUser configUser = configHandler.readConfig(null);

        if(configUser != null) return true;

        return false;
    }

    /**
     * Abfrage ob ein UserConfig bereits existiert über die isMainUserInConfig() ansonsten Fehler
     * Wenn User bereits in Klasse existiert dann Instanz merken, ansonsten einen neuen mit der Basis der Config anlegen
     * @return IUser
     * @throws UserNotExistsException wird geworfen, wenn noch kein User in der Config existiert.
     * @throws ConfigHandlerException wird geworfen, wenn etwas im ConfigHandler nicht Funktioniert hat...
     */
    @Override
    public IUser getMainUser() throws ConfigHandlerException, UserNotExistsException {
        if(!isMainUserInConfig()) throw new UserNotExistsException("Fehler: Es wurde noch kein Hauptbenutzer angelegt!");

        if(user == null){
            ConfigUser configUser = configHandler.readConfig();
            user = createUserFormConfig(configUser);
        }
        return user;
    }

    private IUser createUserFormConfig(ConfigUser configUser) {
        IUser user = new User(configUser.getName(), configUser.getType());
        return user;
    }

    private void createConfigFromUser(IUser user) throws ConfigHandlerException, UserAlreadyExistsException{
        if(isMainUserInConfig()) throw new UserAlreadyExistsException("Es existiert bereits ein Hauptbenutzer in der Config");
        
        ConfigUser configUser = new ConfigUser(user.getName(), user.getUserType());
        configHandler.writeConfig(configUser);
    }
}
