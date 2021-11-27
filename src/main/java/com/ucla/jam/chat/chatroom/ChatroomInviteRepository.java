package com.ucla.jam.chat.chatroom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomInviteRepository {

    /**
     * Get all chat rooms that the given user is a member of.
     * @param userId User UUID
     * @return UUIDs of chat rooms
     */
    List<UUID> getAll(UUID userId);

    /**
     * Create an invite to the given chat room from given source user to given target user. Does nothing
     * if an invite already exists.
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     * @param roomId Room UUID
     */
    void inviteUser(UUID sourceId, UUID targetId, UUID roomId);

    /**
     * Remove invite for given user and given chatroom. Does nothing if no such invite exists.
     * @param targetId Target user UUID
     * @param roomId Room UUID
     */
    void uninviteUser(UUID targetId, UUID roomId);

    /**
     * Get the user who invited the given user to the given chatroom.
     * @param userId Source user UUID
     * @param roomId Room UUID
     * @return Empty optional if the given user has not been invited to the given chatroom
     */
    Optional<UUID> inviter(UUID userId, UUID roomId);
}
