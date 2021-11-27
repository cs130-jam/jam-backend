package com.ucla.jam.user;

import com.ucla.jam.util.Distance;
import com.ucla.jam.util.Location;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

abstract class UserRepositoryContract {

    public abstract UserRepository rep();

    @Test
    void emptyForRandom() {
        assertThat(rep().find(UUID.randomUUID()))
                .isEmpty();
    }

    @Test
    void insertOnce() {
        UUID userId = UUID.randomUUID();
        rep().insert(emptyUser(userId, ""));

        assertThat(rep().find(userId)).isPresent();
    }

    @Test
    void insertOverwrite() {
        UUID userId = UUID.randomUUID();
        rep().insert(emptyUser(userId, "name1"));
        rep().insert(emptyUser(userId, "name2"));

        assertThat(rep().find(userId)).isPresent();
        assertThat(rep().find(userId).get().getProfile().getFirstName())
                .isEqualTo("name2");
    }

    private User emptyUser(UUID userId, String name) {
        return new User(userId,
                new User.Profile(
                        name,
                        "",
                        "",
                        new Location("", ""),
                        "",
                        List.of(),
                        List.of()
                ), new User.Preferences(
                        new Distance(100, Distance.Unit.MILES),
                List.of()));
    }
}
