package com.ucla.jam.user.authentication;

import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

/**
 * Manages authentication and creation of new user credentials.
 */
@RequiredArgsConstructor
public class AuthenticationManager {

    private final CredentialsRepository credentialsRepository;
    private final SessionTokenResolver tokenResolver;

    /**
     * Get session token for given credentials
     * @param credentials User login credentials
     * @return Empty optional if no user matches given credentials, sesion token otherwise
     */
    public Optional<SessionToken> loginUser(Credentials credentials) {
        return credentialsRepository.userForCredentials(credentials)
                .map(SessionInfo::new)
                .map(tokenResolver::toToken);
    }

    /**
     * Creates user authentication entry for given credentials and given user
     * @param userId User UUID
     * @param credentials User login credentials
     * @return Session token of created user
     */
    public SessionToken signupUser(UUID userId, Credentials credentials) {
        credentialsRepository.addUser(userId, credentials);
        return tokenResolver.toToken(new SessionInfo(userId));
    }

    /**
     * Checks if a user exists with given login credentials
     * @param partialCredentials Credentials to check for. These credentials may be missing some fields in some cases.
     *                           Check if the implementing subclass of Credentials includes a method for getting partial credentials.
     * @return True if the user exits, false otherwise
     */
    public boolean isUserExist(Credentials partialCredentials) {
        return credentialsRepository.isUserExist(partialCredentials);
    }
}
