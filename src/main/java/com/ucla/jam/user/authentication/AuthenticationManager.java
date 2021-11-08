package com.ucla.jam.user.authentication;

import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticationManager {

    private final CredentialsRepository credentialsRepository;
    private final SessionTokenResolver tokenResolver;
    private final String passwordSalt;

    public Optional<SessionToken> loginUser(Credentials credentials) {
        return credentialsRepository.userForCredentials(credentials)
                .map(SessionInfo::new)
                .map(tokenResolver::toToken);
    }

    public SessionToken signupUser(UUID userId, Credentials credentials) {
        credentialsRepository.addUser(userId, credentials);
        return tokenResolver.toToken(new SessionInfo(userId));
    }

    public boolean isUserExist(Credentials partialCredentials) {
        return credentialsRepository.isUserExist(partialCredentials);
    }

    public InternalCredentials internal(String username, String password) {
        return new InternalCredentials(username, InternalCredentials.hashPassword(password, passwordSalt));
    }
}