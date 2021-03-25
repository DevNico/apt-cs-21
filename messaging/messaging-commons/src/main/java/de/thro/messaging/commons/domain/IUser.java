package de.thro.messaging.commons.domain;

public interface IUser {
    String getName();
    void setName(String name);
    UserType getUserType();
    void setUserType(UserType userType);
    IUser getUserFromConfig(IConfigUser configUser);
    IConfiguser converToConfigUser(IUser user);
}
