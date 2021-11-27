package com.ucla.jam.notifications.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationData;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationData;
import com.ucla.jam.util.jooq.JsonConverter;

/**
 * Notification data DTO for communication with database.
 * Implementing subclasses must add a @JsonSubType.Type in order to properly serialize and deserialize.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FriendRequestNotificationData.class, name = FriendRequestNotificationData.TYPE),
        @JsonSubTypes.Type(value = GroupInviteNotificationData.class, name = GroupInviteNotificationData.TYPE)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface NotificationData {
    /**
     * Unique string identifying the kind of NotificationData
     * This value must match the name specified in @JsonSubTypes.Type
     * @return Notification data type
     */
    String getType();

    class Converter extends JsonConverter<NotificationData> {}
}
