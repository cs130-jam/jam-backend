package com.ucla.jam.notifications;

import com.ucla.jam.chat.chatroom.ChatroomInviteListener;
import com.ucla.jam.friends.FriendRequestListener;
import com.ucla.jam.user.User;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationManager implements FriendRequestListener, ChatroomInviteListener {

    private final NotificationsRepository notificationsRepository;
    private final Clock clock;

    public List<Notification> getAll(UUID userId) {
        return notificationsRepository.getAll(userId);
    }

    public void friendRequested(User sourceUser, User targetUser) {
        notificationsRepository.insert(new Notification(
                UUID.randomUUID(),
                targetUser.getId(),
                String.format("%s would like to jam with you", sourceUser.getProfile().getFirstName()),
                clock.instant()
        ));
    }

    public void invitedToGroup(UUID sourceId, UUID targetId, UUID roomId) {
        notificationsRepository.insert(new Notification(
                UUID.randomUUID(),
                targetId,
                String.format("%s invited you to %s", sourceUser.getProfile().getFirstName(), room.getInfo().getName()),
                clock.instant()
        ));
    }

    public void acceptNotification(UUID notificationId, UUID userId) {
        Notification notification = notificationsRepository.getAll(userId)
                .stream()
                .filter(n -> n.getId().equals(notificationId))
                .findAny()
                .orElseThrow(UnknownNotificationException::new);

        removeNotification(notificationId, userId);
    }

    public void rejectNotification(UUID notificationId, UUID userId) {
        removeNotification(notificationId, userId);
    }

    public void removeNotification(UUID id, UUID userId) {
        notificationsRepository.remove(id, userId);
    }
}
