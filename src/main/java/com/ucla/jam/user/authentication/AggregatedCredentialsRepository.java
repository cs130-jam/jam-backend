package com.ucla.jam.user.authentication;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
}
