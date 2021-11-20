package com.ucla.jam.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public Optional<User> find(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public void insert(User user) {
        data.put(user.getId(), user);
    }
}
