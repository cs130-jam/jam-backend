package com.ucla.jam.notifications.data;

import com.ucla.jam.notifications.Notification;

/**
 * Handler for notifications being accepted or rejected.
 * Implementing subclasses should cast the notification to the relevant subtype.
 * Each implementing subclass should add an instance to the map in {@link AggregatedNotificationHandler}
 */
public interface NotificationHandler {
    /**
     * Called when the given notification is accepted.
     * @param notification The notification to accept
     */
    void accept(Notification notification);

    /**
     * Called when the given notification is rejected.
     * @param notification The notification to reject
     */
    void reject(Notification notification);
}
