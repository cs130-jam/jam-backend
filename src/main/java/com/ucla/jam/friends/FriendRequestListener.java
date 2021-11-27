package com.ucla.jam.friends;

import java.util.UUID;

public interface FriendRequestListener {
    /**
     * Called when a friend request is created from source user to target user.
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     */
    void friendRequested(UUID sourceId, UUID targetId);
}
