package de.thro.messaging.commons.domain;

import java.util.Objects;

public class User {
    private final String name;
    private final UserType userType;

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

    public UserType getUserType() {
        return this.userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var user = (User) o;
        return Objects.equals(getName(), user.getName()) && Objects.equals(getUserType(), user.getUserType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUserType());
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userType=" + userType +
                '}';
    }
}
