package com.ucla.jam.notifications;

import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.friends.*;
import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.notifications.data.AggregatedNotificationHandler;
import com.ucla.jam.notifications.data.NotificationHandler;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationData;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationHandler;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationData;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class NotificationManagerUnitTest {

    private final NotificationsRepository notificationsRepository = new InMemoryNotificationsRepository();

    @Test
    void notifiesForFriendRequest() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID friendSource = UUID.randomUUID();

        FriendManager friendManager = Mockito.mock(FriendManager.class);
        Mockito.when(friendManager.tryFriend(friendSource)).thenReturn(FriendManager.FriendResult.ACCEPTED);

        FriendManagerFactory friendManagerFactory = Mockito.mock(FriendManagerFactory.class);
        Mockito.when(friendManagerFactory.forUser(userId)).thenReturn(friendManager);

        ChatManager chatManager = Mockito.mock(ChatManager.class);

        NotificationHandler notificationHandler = new AggregatedNotificationHandler(Map.of(
                FriendRequestNotificationData.TYPE, new FriendRequestNotificationHandler(friendManagerFactory),
                GroupInviteNotificationData.TYPE, new GroupInviteNotificationHandler(chatManager)
        ));
        NotificationManager notificationManager = new NotificationManager(notificationsRepository, notificationHandler);

        notificationsRepository.insert(new Notification(
                id,
                userId,
                "test",
                new FriendRequestNotificationData(friendSource),
                true,
                true,
                Clock.systemUTC().instant()
        ));

        notificationManager.acceptNotification(id, userId);
        Mockito.verify(friendManager).tryFriend(friendSource);
        assertThat(notificationsRepository.getAll(userId))
                .isEmpty();
    }

    @Test
    void removesForRejectFriendRequest() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID friendSource = UUID.randomUUID();

        FriendManager friendManager = Mockito.mock(FriendManager.class);
        Mockito.doNothing().when(friendManager).cancelRequest(userId);
        FriendManagerFactory friendManagerFactory = Mockito.mock(FriendManagerFactory.class);
        Mockito.when(friendManagerFactory.forUser(friendSource)).thenReturn(friendManager);

        ChatManager chatManager = Mockito.mock(ChatManager.class);

        NotificationHandler notificationHandler = new AggregatedNotificationHandler(Map.of(
                FriendRequestNotificationData.TYPE, new FriendRequestNotificationHandler(friendManagerFactory),
                GroupInviteNotificationData.TYPE, new GroupInviteNotificationHandler(chatManager)
        ));
        NotificationManager notificationManager = new NotificationManager(notificationsRepository, notificationHandler);

        notificationsRepository.insert(new Notification(
                id,
                userId,
                "test",
                new FriendRequestNotificationData(friendSource),
                true,
                true,
                Clock.systemUTC().instant()
        ));

        notificationManager.rejectNotification(id, userId);
        Mockito.verify(friendManager).cancelRequest(userId);
        assertThat(notificationsRepository.getAll(userId))
                .isEmpty();
    }

    @Test
    void notifiesForGroupInvite() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID chatroomId = UUID.randomUUID();

        FriendManagerFactory friendManagerFactory = Mockito.mock(FriendManagerFactory.class);

        ChatManager chatManager = Mockito.mock(ChatManager.class);
        Mockito.doNothing().when(chatManager).joinChatroom(userId, chatroomId);
        Mockito.doNothing().when(chatManager).rejectInvite(userId, chatroomId);

        NotificationHandler notificationHandler = new AggregatedNotificationHandler(Map.of(
                FriendRequestNotificationData.TYPE, new FriendRequestNotificationHandler(friendManagerFactory),
                GroupInviteNotificationData.TYPE, new GroupInviteNotificationHandler(chatManager)
        ));
        NotificationManager notificationManager = new NotificationManager(notificationsRepository, notificationHandler);

        notificationsRepository.insert(new Notification(
                id,
                userId,
                "test",
                new GroupInviteNotificationData(chatroomId),
                true,
                true,
                Clock.systemUTC().instant()
        ));

        notificationManager.acceptNotification(id, userId);
        Mockito.verify(chatManager).joinChatroom(userId, chatroomId);
        assertThat(notificationsRepository.getAll(userId))
                .isEmpty();
    }

    @Test
    void removeForRejectGroupInvite() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID chatroomId = UUID.randomUUID();

        FriendManagerFactory friendManagerFactory = Mockito.mock(FriendManagerFactory.class);

        ChatManager chatManager = Mockito.mock(ChatManager.class);
        Mockito.doNothing().when(chatManager).joinChatroom(userId, chatroomId);
        Mockito.doNothing().when(chatManager).rejectInvite(userId, chatroomId);

        NotificationHandler notificationHandler = new AggregatedNotificationHandler(Map.of(
                FriendRequestNotificationData.TYPE, new FriendRequestNotificationHandler(friendManagerFactory),
                GroupInviteNotificationData.TYPE, new GroupInviteNotificationHandler(chatManager)
        ));
        NotificationManager notificationManager = new NotificationManager(notificationsRepository, notificationHandler);

        notificationsRepository.insert(new Notification(
                id,
                userId,
                "test",
                new GroupInviteNotificationData(chatroomId),
                true,
                true,
                Clock.systemUTC().instant()
        ));

        notificationManager.rejectNotification(id, userId);
        Mockito.verify(chatManager).rejectInvite(chatroomId, userId);
        assertThat(notificationsRepository.getAll(userId))
                .isEmpty();
    }
}
