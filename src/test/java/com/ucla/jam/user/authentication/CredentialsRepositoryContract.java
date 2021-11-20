package com.ucla.jam.user.authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public abstract class CredentialsRepositoryContract {

    public abstract CredentialsRepository rep();

    @Test
    void emptyForRandom() {
        assertThat(rep().userForCredentials(randomCredentials()))
                .isEmpty();
    }

    @Test
    void inserts() {
        InternalCredentials credentials = randomCredentials();
        rep().addUser(UUID.randomUUID(), credentials);

        assertThat(rep().userForCredentials(credentials))
                .isPresent();
    }

    @Test
    void insertOverwrite() {
        InternalCredentials credentials = randomCredentials();
        UUID id2 = UUID.randomUUID();
        rep().addUser(UUID.randomUUID(), credentials);
        rep().addUser(id2, credentials);

        assertThat(rep().userForCredentials(credentials))
                .isPresent();
        assertThat(rep().userForCredentials(credentials).get())
                .isEqualTo(id2);
    }

    @Test
    void noUserExistsForRandom() {
        assertThat(rep().isUserExist(randomCredentials()))
                .isFalse();
    }

    @Test
    void userExistsIfInserted() {
        InternalCredentials credentials = randomCredentials();
        rep().addUser(UUID.randomUUID(), credentials);
        assertThat(rep().isUserExist(credentials))
                .isTrue();
    }

    private InternalCredentials randomCredentials() {
        return new InternalCredentials(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
