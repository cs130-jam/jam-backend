package com.ucla.jam.notifications;

import com.ucla.jam.chat.chatroom.Chatroom;
import com.ucla.jam.chat.chatroom.ChatroomRepository;
import com.ucla.jam.chat.chatroom.GroupInviteListener;
import com.ucla.jam.chat.chatroom.UnknownChatroomException;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationData;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
public class GroupInviteNotificationListener implements GroupInviteListener {

    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final NotificationsRepository notificationsRepository;
    private final Clock clock;

    /**
     * Triggered when the target user is invited to the given room by the source user
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     * @param roomId Room UUID
     */
    @Override
    public void invitedToGroup(UUID sourceId, UUID targetId, UUID roomId) {
        Chatroom room = chatroomRepository.get(roomId)
                .orElseThrow(UnknownChatroomException::new);
        User sourceUser = userRepository.find(sourceId)
                .orElseThrow(UnknownUserException::new);
        notificationsRepository.insert(new Notification(
                UUID.randomUUID(),
                targetId,
                String.format("%s invited you to %s", sourceUser.getProfile().getFirstName(), room.getInfo().getName()),
                new GroupInviteNotificationData(roomId),
                true,
                true,
                clock.instant()
        ));
    }
}
