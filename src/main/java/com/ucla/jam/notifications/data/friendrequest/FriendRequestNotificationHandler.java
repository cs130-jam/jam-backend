package com.ucla.jam.notifications.data.friendrequest;

import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.notifications.Notification;
import com.ucla.jam.notifications.data.NotificationHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendRequestNotificationHandler implements NotificationHandler {

    private final FriendManagerFactory friendManagerFactory;

    @Override
    public void accept(Notification notification) {
        FriendRequestNotificationData data = (FriendRequestNotificationData) notification.getData();
        friendManagerFactory.forUser(notification.getUserId()).tryFriend(data.getSourceId());
    }

    @Override
    public void reject(Notification notification) {
        FriendRequestNotificationData data = (FriendRequestNotificationData) notification.getData();
        friendManagerFactory.forUser(data.getSourceId()).cancelRequest(notification.getUserId());
    }
}
