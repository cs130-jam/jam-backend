package com.ucla.jam.notifications;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepository {

    /**
     * Get all notifications for given user.
     * @param userId User UUID
     * @return List of notifications
     */
    List<Notification> getAll(UUID userId);

    /**
     * Insert notification into repository.
     * Will do nothing if notification with given id and user already exists.
     * @param notification Notification to insert
     */
    void insert(Notification notification);

    /**
     * Remove notification with given id and user.
     * Will do nothing if notification does not exist.
     * @param id Notification UUID
     * @param userId User UUID
     */
    void remove(UUID id, UUID userId);
}
