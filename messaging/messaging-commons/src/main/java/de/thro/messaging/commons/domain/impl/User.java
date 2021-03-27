package de.thro.messaging.commons.domain.impl;

import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;

public class User implements IUser {
    private String name;
    private UserType userType;

    public  User (){

    }

    public  User (String name, UserType type){
        this.name = name;
        this.userType = type;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public UserType getUserType() {
        return null;
    }

    @Override
    public void setUserType(UserType userType) {

    }
}
