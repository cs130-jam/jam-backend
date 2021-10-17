package com.ucla.jam.user.authentication;

import java.util.Optional;
import java.util.UUID;

public interface CredentialsRepository {
    Optional<UUID> userForCredentials(Credentials credentials);
    void addUser(UUID id, Credentials credentials);
}
