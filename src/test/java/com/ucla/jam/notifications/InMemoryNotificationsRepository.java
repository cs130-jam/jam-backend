package com.ucla.jam.notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class InMemoryNotificationsRepository implements NotificationsRepository {

    private List<Notification> data = new ArrayList<>();

    @Override
    public List<Notification> getAll(UUID userId) {
        return data.stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .collect(toList());
    }

    @Override
    public void insert(Notification notification) {
        if (contains(notification.getId(), notification.getUserId())) return;
        data.add(notification);
    }

    @Override
    public void remove(UUID id, UUID userId) {
        data = data.stream()
                .filter(notification -> !notification.getId().equals(id) || !notification.getUserId().equals(userId))
                .collect(toList());
    }

    private boolean contains(UUID id, UUID userId) {
        return getAll(userId).stream()
                .anyMatch(notification -> notification.getId().equals(id));
    }
}
