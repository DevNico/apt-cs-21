package de.thro.messaging.commons.UserManager;

import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

import javax.naming.ConfigurationException;

public class UserManager implements IUserManager {


    @Override
    public void createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigHandlerException {

    }

    @Override
    public boolean isUserInConfig() throws ConfigHandlerException {
        return false;
    }

    @Override
    public IUser getMainUser() throws ConfigHandlerException, UserNotExistsException {
        return null;
    }

    @Override
    public IUser createUser(String name, UserType type) {
        return null;
    }
}
