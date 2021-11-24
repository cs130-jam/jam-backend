package com.ucla.jam.friends;

import java.util.UUID;

public interface FriendRequestListener {
    void friendRequested(UUID sourceId, UUID targetId);
}
