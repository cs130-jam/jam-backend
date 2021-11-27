package com.ucla.jam.user.authentication;

import java.util.Optional;
import java.util.UUID;

public interface CredentialsRepository {

    /**
     * Get user for given credentials.
     * @param credentials User login credentials
     * @return Empty optional if credentials do not match any user, user UUID otherwise
     */
    Optional<UUID> userForCredentials(Credentials credentials);

    /**
     * Add a user's credentials for given user id.
     * Does nothing if given user credentials are already present.
     * @param id User UUID
     * @param credentials User login credentials
     */
    void addUser(UUID id, Credentials credentials);

    /**
     * Check if a user exists with given credentials.
     * @param partialCredentials Credentials to check for. May be missing some fields depending on credentials kind
     * @return True if user exists, false otherwise
     */
    boolean isUserExist(Credentials partialCredentials);
}
