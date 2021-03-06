package com.ucla.jam.user.authentication;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Handler for all credential types.
 * Using the {@link Credentials#getKind()}, the relevant credentials handler is called
 */
@RequiredArgsConstructor
public class AggregatedCredentialsRepository implements CredentialsRepository {

    private final Map<Credentials.Kind, CredentialsRepository> repositoryMap;

    @Override
    public Optional<UUID> userForCredentials(Credentials credentials) {
        return Optional.ofNullable(repositoryMap.get(credentials.getKind()))
                .flatMap(repository -> repository.userForCredentials(credentials));
    }

    @Override
    public void addUser(UUID id, Credentials credentials) {
        Optional.ofNullable(repositoryMap.get(credentials.getKind()))
                .ifPresent(repository -> repository.addUser(id, credentials));
    }

    @Override
    public boolean isUserExist(Credentials partialCredentials) {
        return Optional.ofNullable(repositoryMap.get(partialCredentials.getKind()))
                .map(repository -> repository.isUserExist(partialCredentials))
                .orElse(false);
    }
}
