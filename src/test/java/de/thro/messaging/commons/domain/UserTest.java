package de.thro.messaging.commons.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("User model tests")
class UserTest {
    @ParameterizedTest
    @EnumSource(UserType.class)
    void testToString(UserType type) {
        final var user = new User("username", type);
        assertThat(String.format("User{name='username', userType=%s}", type.toString()), equalTo(user.toString()));
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(User.class).verify();
    }
}