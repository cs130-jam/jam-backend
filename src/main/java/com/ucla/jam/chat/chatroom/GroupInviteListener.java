package com.ucla.jam.chat.chatroom;

import java.util.UUID;

public interface GroupInviteListener {
    void invitedToGroup(UUID sourceId, UUID targetId, UUID roomId);
}
