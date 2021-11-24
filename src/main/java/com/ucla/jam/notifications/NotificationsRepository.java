package com.ucla.jam.notifications;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepository {
    List<Notification> getAll(UUID userId);
    void insert(Notification notification);
    void remove(UUID id, UUID userId);
}
