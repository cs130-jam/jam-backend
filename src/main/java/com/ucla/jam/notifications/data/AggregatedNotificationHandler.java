package com.ucla.jam.notifications.data;

import com.ucla.jam.notifications.Notification;
import com.ucla.jam.notifications.UnknownNotificationException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

/**
 * Handler for all notification types.
 * Using the type of the notification, this will trigger the relevant {@link NotificationHandler}.
 */
@RequiredArgsConstructor
public class AggregatedNotificationHandler implements NotificationHandler {

    private final Map<String, NotificationHandler> handlerMap;

    @Override
    public void accept(Notification notification) {
        getHandler(notification).accept(notification);
    }

    @Override
    public void reject(Notification notification) {
        getHandler(notification).reject(notification);
    }

    private NotificationHandler getHandler(Notification notification) {
        String type = notification.getData().getType();
        return Optional.ofNullable(handlerMap.get(type))
                .orElseThrow(UnknownNotificationException::new);
    }
}
