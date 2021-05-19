package de.thro.messaging.commons.domain;

public class User {
    private String name;
    private UserType userType;

    public User() {
    }

    public User(String name, UserType type) throws IllegalArgumentException {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Name has to have a Value. Blank or empty string is not permitted");
        }
        this.name = name;
        if (type == null) {
            throw new IllegalArgumentException("Type is required and can not be null");
        }
        this.userType = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
