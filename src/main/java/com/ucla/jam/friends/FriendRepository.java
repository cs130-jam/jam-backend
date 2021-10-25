package com.ucla.jam.friends;

import java.util.List;
import java.util.UUID;

public interface FriendRepository {
    List<UUID> getAll(UUID user);
    void friend(UUID userA, UUID userB);
    void unfriend(UUID userA, UUID userB);
}
