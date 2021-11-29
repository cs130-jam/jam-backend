package com.ucla.jam.notifications;

import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationData;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public abstract class NotificationsRepositoryContract {

    abstract NotificationsRepository rep();

    @Test
    void emptyForRandom() {
        assertThat(rep().getAll(UUID.randomUUID()))
                .isEmpty();
    }

    @Test
    void insertsNotifications() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        rep().insert(new Notification(
                notificationId,
                userId,
                "",
                new FriendRequestNotificationData(UUID.randomUUID()),
                true,
                true,
                Clock.systemUTC().instant()));

        assertThat(rep().getAll(userId))
                .hasSize(1);
    }

    @Test
    void removesNotification() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        rep().insert(new Notification(
                notificationId,
                userId,
                "",
                new FriendRequestNotificationData(UUID.randomUUID()),
                true,
                true,
                Clock.systemUTC().instant()));

        rep().remove(notificationId, userId);
        assertThat(rep().getAll(userId))
                .isEmpty();
    }

    @Test
    void doesNothingOnDuplicate() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        rep().insert(new Notification(
                notificationId,
                userId,
                "old thing",
                new FriendRequestNotificationData(UUID.randomUUID()),
                true,
                true,
                Clock.systemUTC().instant()));

        rep().insert(new Notification(
                notificationId,
                userId,
                "new thing",
                new FriendRequestNotificationData(UUID.randomUUID()),
                true,
                true,
                Clock.systemUTC().instant()));


        assertThat(rep().getAll(userId))
                .hasSize(1);
        assertThat(rep().getAll(userId).get(0).getTitle())
                .isEqualTo("old thing");
    }

    @Test
    void doesNothingOnNothingToRemove() {
        UUID userId = UUID.randomUUID();
        rep().remove(UUID.randomUUID(), userId);
        assertThat(rep().getAll(userId))
                .isEmpty();
    }
}
