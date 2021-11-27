package com.ucla.jam.friends;

import java.util.List;
import java.util.UUID;

public interface FriendRepository {

    /**
     * Get all users who are friends with given user.
     * @param user User UUID
     * @return List of friend user UUIDs
     */
    List<UUID> getAll(UUID user);

    /**
     * Friend userA and userB.
     * Does nothing if the users are already friends.
     * @param userA User UUID
     * @param userB User UUID
     */
    void friend(UUID userA, UUID userB);

    /**
     * Unfriend userA and userB.
     * Does nothing if users are not friends.
     * @param userA User UUID
     * @param userB User UUID
     */
    void unfriend(UUID userA, UUID userB);
}
