package com.ucla.jam.user;

import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.user.authentication.Credentials;
import com.ucla.jam.user.authentication.CredentialsRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginManager {

    private final CredentialsRepository credentialsRepository;
    private final SessionTokenResolver tokenResolver;

    public Optional<SessionToken> loginUser(Credentials credentials) {
        return credentialsRepository.userForCredentials(credentials)
                .map(SessionInfo::new)
                .map(tokenResolver::toToken);
    }

    public Optional<SessionToken> signupUser(UUID userId, Credentials credentials) {
        if (credentialsRepository.userForCredentials(credentials).isPresent()) {
            return Optional.empty();
        } else {
            credentialsRepository.addUser(userId, credentials);
            return Optional.of(tokenResolver.toToken(new SessionInfo(userId)));
        }
    }
}
