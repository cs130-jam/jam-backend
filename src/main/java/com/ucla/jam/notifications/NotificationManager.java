package com.ucla.jam.notifications;

import com.ucla.jam.notifications.data.NotificationHandler;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Handles notifications being accepted, rejected, and removed.
 */
@RequiredArgsConstructor
public class NotificationManager {

    private final NotificationsRepository notificationsRepository;
    private final NotificationHandler notificationHandler;

    /**
     * Get all notifications for the given user
     * @param userId User UUID
     * @return List of notifications
     */
    public List<Notification> getAll(UUID userId) {
        return notificationsRepository.getAll(userId);
    }

    /**
     * Perform accept action for given notification and user.
     * Removes notification from user's list.
     * Throws {@link UnknownNotificationException} if the given user does not have the given notification.
     * @param notificationId Notification UUID
     * @param userId User UUID
     */
    public void acceptNotification(UUID notificationId, UUID userId) {
        Notification notification = getNotification(notificationId, userId);
        notificationHandler.accept(notification);
        removeNotification(notificationId, userId);
    }

    /**
     * Perform reject action for given notification and user.
     * Removes notification from user's list.
     * Throws {@link UnknownNotificationException} if the given user does not have the given notification.
     * @param notificationId Notification UUID
     * @param userId User UUID
     */
    public void rejectNotification(UUID notificationId, UUID userId) {
        Notification notification = getNotification(notificationId, userId);
        notificationHandler.reject(notification);
        removeNotification(notificationId, userId);
    }

    /**
     * Removes notification from user's list without performing any action.
     * Throws {@link UnknownNotificationException} if the given user does not have the given notification.
     * @param id Notification UUID
     * @param userId User UUID
     */
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
