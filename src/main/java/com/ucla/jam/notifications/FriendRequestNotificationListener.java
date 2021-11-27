package com.ucla.jam.notifications;

import com.ucla.jam.friends.FriendRequestListener;
import com.ucla.jam.notifications.data.friendrequest.FriendRequestNotificationData;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
public class FriendRequestNotificationListener implements FriendRequestListener {

    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;
    private final Clock clock;

    /**
     * Called when a friend request is sent from source user to target user.
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     */
    @Override
    public void friendRequested(UUID sourceId, UUID targetId) {
        User sourceUser = userRepository.find(sourceId)
                .orElseThrow(UnknownUserException::new);
        notificationsRepository.insert(new Notification(
                UUID.randomUUID(),
                targetId,
                String.format("%s would like to jam with you", sourceUser.getProfile().getFirstName()),
                new FriendRequestNotificationData(sourceUser.getId()),
                true,
                true,
                clock.instant()
        ));
    }
}
