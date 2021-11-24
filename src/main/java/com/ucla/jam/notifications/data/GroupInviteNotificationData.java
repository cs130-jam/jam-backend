package com.ucla.jam.notifications.data;

import lombok.Value;

import java.util.UUID;

@Value
public class GroupInviteNotificationData implements NotificationData {
    public static final String TYPE = "GROUP_INVITE";

    UUID roomId;

    @Override
    public String getType() {
        return TYPE;
    }
}
