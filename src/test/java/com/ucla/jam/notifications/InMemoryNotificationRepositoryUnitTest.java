package com.ucla.jam.notifications;

public class InMemoryNotificationRepositoryUnitTest extends NotificationsRepositoryContract {

    private final NotificationsRepository notificationsRepository = new InMemoryNotificationsRepository();

    @Override
    NotificationsRepository rep() {
        return notificationsRepository;
    }
}
