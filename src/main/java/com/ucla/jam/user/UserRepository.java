package com.ucla.jam.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    /**
     * Find user with given id
     * @param userId User UUID
     * @return Empty optional if no user exists with that UUID, optional of user otherwise
     */
    Optional<User> find(UUID userId);

    /**
     * Insert user, or update record if user with given id already exists.
     * @param user User DTO
     */
    void insert(User user);
}
