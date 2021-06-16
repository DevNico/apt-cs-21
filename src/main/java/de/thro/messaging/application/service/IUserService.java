package de.thro.messaging.application.service;

import de.thro.messaging.domain.enums.UserType;
import de.thro.messaging.domain.models.User;

public interface IUserService {

    User getUserName();
    void setUserName(String name);
    UserType getUserType();
    void setUserType(UserType type);

}
