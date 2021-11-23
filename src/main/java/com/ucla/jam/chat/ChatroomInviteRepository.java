package com.ucla.jam.chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomInviteRepository {
    List<UUID> getAll(UUID userId);
    void inviteUser(UUID sourceId, UUID targetId, UUID roomId);
    void uninviteUser(UUID targetId, UUID roomId);
    Optional<UUID> inviter(UUID userId, UUID roomId);
}
