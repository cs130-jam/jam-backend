package com.ucla.jam.user.authentication;

import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.util.ObjectMapperProvider;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthenticationManagerUnitTest {

    private final AuthenticationManager authenticationManager = new AuthenticationManager(
            new InMemoryInternalCredentialsRepository(),
            new SessionTokenResolver(
                    "aksdhgagdhajshgdjajhdkashdaksjdhashgdajgsdh",
                    Duration.ofHours(6),
                    ObjectMapperProvider.get(),
                    Clock.systemUTC()
            ),
            "asjhdakjshdkajshd"
    );

    @Test
    void failForNonUser() {
        assertThat(authenticationManager.loginUser(randomCredentials()))
                .isEmpty();
    }

    @Test
    void notExistForNonUser() {
        assertThat(authenticationManager.isUserExist(randomCredentials()))
                .isFalse();
    }

    @Test
    void existForUser() {
        InternalCredentials credentials = randomCredentials();
        authenticationManager.signupUser(UUID.randomUUID(), credentials);
        assertThat(authenticationManager.isUserExist(credentials))
                .isTrue();
        assertThat(authenticationManager.loginUser(credentials))
                .isPresent();
    }

    @Test
    void failForEmptyUsername() {
        assertThrows(InvalidSignupException.class,
                () -> authenticationManager.internal("", UUID.randomUUID().toString()));
    }

    @Test
    void failForShortPassword() {
        assertThrows(InvalidSignupException.class,
                () -> authenticationManager.internal(UUID.randomUUID().toString(), "small"));
    }

    private InternalCredentials randomCredentials() {
        return new InternalCredentials(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

}
