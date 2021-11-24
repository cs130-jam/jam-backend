package com.ucla.jam.notifications.data.groupinvite;

import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.notifications.Notification;
import com.ucla.jam.notifications.data.NotificationHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupInviteNotificationHandler implements NotificationHandler {

    private final ChatManager chatManager;

    @Override
    public void accept(Notification notification) {
        GroupInviteNotificationData data = (GroupInviteNotificationData) notification.getData();
        chatManager.joinChatroom(notification.getUserId(), data.getRoomId());
    }

    @Override
    public void reject(Notification notification) {
        GroupInviteNotificationData data = (GroupInviteNotificationData) notification.getData();
        chatManager.rejectInvite(data.getRoomId(), notification.getUserId());
    }
}
