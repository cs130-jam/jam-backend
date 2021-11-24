package com.ucla.jam.notifications;

import com.ucla.jam.notifications.data.NotificationData;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Notification {
    UUID id;
    UUID userId;
    String title;
    NotificationData data;
    boolean canAccept;
    boolean canReject;
    Instant at;
}
