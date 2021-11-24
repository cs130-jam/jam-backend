package com.ucla.jam.notifications;

import com.ucla.jam.notifications.data.NotificationHandler;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationManager {

    private final NotificationsRepository notificationsRepository;
    private final NotificationHandler notificationHandler;

    public List<Notification> getAll(UUID userId) {
        return notificationsRepository.getAll(userId);
    }

    public void acceptNotification(UUID notificationId, UUID userId) {
        Notification notification = getNotification(notificationId, userId);
        notificationHandler.accept(notification);
        removeNotification(notificationId, userId);
    }

    public void rejectNotification(UUID notificationId, UUID userId) {
        Notification notification = getNotification(notificationId, userId);
        notificationHandler.reject(notification);
        removeNotification(notificationId, userId);
    }

    public void removeNotification(UUID id, UUID userId) {
        notificationsRepository.remove(id, userId);
    }

    private Notification getNotification(UUID notificationId, UUID userId) {
        return notificationsRepository.getAll(userId)
                .stream()
                .filter(n -> n.getId().equals(notificationId))
                .findAny()
                .orElseThrow(UnknownNotificationException::new);
    }
}
