package com.ucla.jam.notifications;

import com.ucla.jam.chat.Chatroom;
import com.ucla.jam.user.User;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import static com.ucla.jam.notifications.Notification.Method.POST;

@RequiredArgsConstructor
public class NotificationManager {

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
                new Notification.Action(
                        POST,
                        String.format("friends/%s", sourceUser.getId())),
                new Notification.Action(
                        POST,
                        String.format("friends/requests/reject?sourceId=%s", sourceUser.getId())),
                clock.instant()
        ));
    }

    public void invitedToGroup(User sourceUser, User targetUser, Chatroom room) {
        notificationsRepository.insert(new Notification(
                UUID.randomUUID(),
                targetUser.getId(),
                String.format("%s invited you to %s", sourceUser.getProfile().getFirstName(), room.getInfo().getName()),
                new Notification.Action(
                        POST,
                        String.format("chatroom/%s/join", room.getId())),
                new Notification.Action(
                        POST,
                        String.format("chatroom/%s/reject", room.getId())),
                clock.instant()
        ));
    }

    public void removeNotification(UUID id, UUID userId) {
        notificationsRepository.remove(id, userId);
    }
}
