package de.thro.messaging.commons.confighandler;
import de.thro.messaging.commons.domain.UserType;

public class ConfigUser implements IConfigHandable{
    private String name;
    private UserType type;

    public ConfigUser(String name, UserType type){
        if(name == null)
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

    @Override
    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
