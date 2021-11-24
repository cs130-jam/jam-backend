package com.ucla.jam.notifications.data.friendrequest;

import com.ucla.jam.notifications.data.NotificationData;
import lombok.Value;

import java.util.UUID;

@Value
public class FriendRequestNotificationData implements NotificationData {
    public static final String TYPE = "FRIEND_REQUEST";

    UUID sourceId;

    @Override
    public String getType() {
        return TYPE;
    }
}
