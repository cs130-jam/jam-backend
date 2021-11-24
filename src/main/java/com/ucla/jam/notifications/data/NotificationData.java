package com.ucla.jam.notifications.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ucla.jam.util.jooq.JsonConverter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FriendRequestNotificationData.class, name = FriendRequestNotificationData.TYPE),
        @JsonSubTypes.Type(value = GroupInviteNotificationData.class, name = GroupInviteNotificationData.TYPE)
})
public interface NotificationData {
    String getType();

    class Converter extends JsonConverter<NotificationData> {}
}
