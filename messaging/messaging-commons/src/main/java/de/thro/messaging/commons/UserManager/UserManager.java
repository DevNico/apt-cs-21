package de.thro.messaging.commons.UserManager;

import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

import javax.naming.ConfigurationException;

public class UserManager implements IUserManager {


    @Override
    public void createMainUser(String name, UserType type) throws UserAlreadyExistsException, ConfigurationException {

    }

    @Override
    public boolean isUserInConfig() throws ConfigurationException {
        return false;
    }

    @Override
    public IUser getMainUser() throws ConfigurationException, UserNotExistsException {
        return null;
    }
}
