package com.ucla.jam.user.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryInternalCredentialsRepository implements CredentialsRepository {

    private final Map<InternalCredentials, UUID> data = new HashMap<>();

    @Override
    public Optional<UUID> userForCredentials(Credentials genericCredentials) {
        InternalCredentials credentials = (InternalCredentials) genericCredentials;
        return Optional.ofNullable(data.get(credentials));
    }

    @Override
    public void addUser(UUID id, Credentials genericCredentials) {
        InternalCredentials credentials = (InternalCredentials) genericCredentials;
        data.put(credentials, id);
    }

    @Override
    public boolean isUserExist(Credentials partialCredentials) {
        InternalCredentials credentials = (InternalCredentials) partialCredentials;
        return data.keySet().stream()
                .anyMatch(key -> key.getUsername().equals(credentials.getUsername()));
    }
}
