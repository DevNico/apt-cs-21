package de.thro.messaging.commons.confighandler;

import de.thro.messaging.domain.enums.UserType;

public class ConfigUser {
    private final String name;
    private final UserType type;

    public ConfigUser(String name, UserType type) {
        if (name == null)
            this.name = "";
        else this.name = name;
        if (type == null)
            this.type = UserType.TEACHER;
        else this.type = type;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }
}
