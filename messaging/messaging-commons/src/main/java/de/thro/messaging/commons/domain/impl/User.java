package de.thro.messaging.commons.domain.impl;

import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

public class User implements IUser {
    private String name;
    private UserType userType;

    public User(){};

    public  User (String name, UserType type) throws IllegalArgumentException{
        if(name == null || name.isEmpty() || name.isBlank()) {throw new IllegalArgumentException("Name has to have a Value. Blank or empty string is not permitted");}
        this.name = name;
        if(type.equals(null)){throw new IllegalArgumentException("Type is required and can not be null");}
        this.userType = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UserType getUserType() {
        return this.userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
