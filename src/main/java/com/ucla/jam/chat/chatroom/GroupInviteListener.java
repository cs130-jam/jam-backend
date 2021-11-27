package com.ucla.jam.chat.chatroom;

import java.util.UUID;

public interface GroupInviteListener {
    /**
     * Message for when source user invites target user to given room
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     * @param roomId Room UUID
     */
    void invitedToGroup(UUID sourceId, UUID targetId, UUID roomId);
}
