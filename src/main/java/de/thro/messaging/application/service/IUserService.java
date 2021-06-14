package de.thro.messaging.application.service;

import de.thro.messaging.domain.enums.UserType;

public interface IUserService {

    String getUserName();
    void setUserName(String name);
    UserType getUserType();
    void setUserType(UserType type);

}
