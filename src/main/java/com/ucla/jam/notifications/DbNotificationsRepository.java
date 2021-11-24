package com.ucla.jam.notifications;

import generated.jooq.tables.records.NotificationsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static generated.jooq.tables.Notifications.NOTIFICATIONS;

@RequiredArgsConstructor
public class DbNotificationsRepository implements NotificationsRepository {

    private final DSLContext context;

    @Override
    public List<Notification> getAll(UUID userId) {
        return context.selectFrom(NOTIFICATIONS)
                .where(NOTIFICATIONS.USERID.eq(userId))
                .fetch(this::fromRecord);
    }

    @Override
    public void insert(Notification notification) {
        context.insertInto(NOTIFICATIONS)
                .set(toRecord(notification))
                .execute();
    }

    @Override
    public void remove(UUID id, UUID userId) {
        context.deleteFrom(NOTIFICATIONS)
                .where(NOTIFICATIONS.USERID.eq(userId)
                        .and(NOTIFICATIONS.ID.eq(id)))
                .execute();
    }

    private Notification fromRecord(NotificationsRecord record) {
        return new Notification(
                record.getId(),
                record.getUserid(),
                record.getTitle(),
                record.getAccept(),
                record.getReject(),
                record.getAt());
    }

    private NotificationsRecord toRecord(Notification notification) {
        return new NotificationsRecord(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getAt(),
                notification.getAccept(),
                notification.getReject());
    }
}
