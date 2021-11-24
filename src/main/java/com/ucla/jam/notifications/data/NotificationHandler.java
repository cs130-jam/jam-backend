package com.ucla.jam.notifications.data;

import com.ucla.jam.notifications.Notification;

public interface NotificationHandler {
    void accept(Notification notification);
    void reject(Notification notification);
}
