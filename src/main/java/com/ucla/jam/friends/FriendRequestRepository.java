package com.ucla.jam.friends;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository {
    void request(UUID sourceUser, UUID targetUser);
    void unrequest(UUID sourceUser, UUID targetUser);
    boolean isRequested(UUID sourceUser, UUID targetUser);
    List<UUID> requestsTo(UUID targetUser);
    List<UUID> requestsFrom(UUID sourceUser);
}
