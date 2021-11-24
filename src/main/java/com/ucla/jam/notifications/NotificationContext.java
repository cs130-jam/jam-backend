package com.ucla.jam.notifications;

import com.ucla.jam.chat.ChatContext;
import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.chat.chatroom.ChatroomRepository;
import com.ucla.jam.chat.chatroom.DbChatroomRepository;
import com.ucla.jam.friends.FriendContext;
import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.notifications.data.AggregatedNotificationHandler;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationData;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationHandler;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationData;
import com.ucla.jam.notifications.data.groupinvite.GroupInviteNotificationHandler;
import com.ucla.jam.user.DbUserRepository;
import com.ucla.jam.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.util.Map;

@Import({
        DbNotificationsRepository.class,
        DbUserRepository.class,
        DbChatroomRepository.class,

        FriendContext.class,
        ChatContext.class
})
public class NotificationContext {

    @Bean
    public FriendRequestNotificationListener friendListener(
            UserRepository userRepository,
            NotificationsRepository notificationsRepository,
            Clock clock
    ) {
        return new FriendRequestNotificationListener(userRepository, notificationsRepository, clock);
    }

    @Bean
    public GroupInviteNotificationListener groupListener(
            UserRepository userRepository,
            ChatroomRepository chatroomRepository,
            NotificationsRepository notificationsRepository,
            Clock clock
    ) {
        return new GroupInviteNotificationListener(userRepository, chatroomRepository, notificationsRepository, clock);
    }

    @Bean
    public NotificationManager notificationManager(
            NotificationsRepository notificationsRepository,
            FriendManagerFactory friendManagerFactory,
            ChatManager chatManager
    ) {
        return new NotificationManager(
                notificationsRepository,
                new AggregatedNotificationHandler(
                        Map.of(
                                FriendRequestNotificationData.TYPE, new FriendRequestNotificationHandler(friendManagerFactory),
                                GroupInviteNotificationData.TYPE, new GroupInviteNotificationHandler(chatManager)
                        )
                )
        );
    }
}
