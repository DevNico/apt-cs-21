package de.thro.messaging.commons.confighandler;

public class ConfigUser {
    private String name;
    private UserType type;

    public ConfigUser(String name, UserType type){
        if(name == null)
            this.name = "";
        else this.name = name;
        if (type == null)
            type = UserType.DOZENT;
        else this.type = type;
    }
}
