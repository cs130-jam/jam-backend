package com.ucla.jam.friends;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository {

    /**
     * Request target user to be friends with source user.
     * Does nothing if request already exists.
     * @param sourceUser Source user UUID
     * @param targetUser Target user UUID
     */
    void request(UUID sourceUser, UUID targetUser);

    /**
     * Cancel request for target user to be friends with source user.
     * Does nothing if request does not exist.
     * @param sourceUser Source user UUID
     * @param targetUser Target user UUID
     */
    void unrequest(UUID sourceUser, UUID targetUser);

    /**
     * Check whether source user has requested to be friends with target user.
     * @param sourceUser Source user UUID
     * @param targetUser Target user UUID
     * @return True if a request exists from source user to target user, false otherwise
     */
    boolean isRequested(UUID sourceUser, UUID targetUser);

    /**
     * List users who have sent a friend request to target user.
     * @param targetUser Target user UUID
     * @return List of user UUIDs
     */
    List<UUID> requestsTo(UUID targetUser);

    /**
     * List users who have been sent a friend request from source user.
     * @param sourceUser Source user UUID
     * @return List of user UUIDs
     */
    List<UUID> requestsFrom(UUID sourceUser);
}
