package com.ucla.jam.friends;

import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FriendManagerUnitTest {

    private final UUID userId = UUID.randomUUID();
    private final FriendManagerFactory friendManagerFactory = new FriendManagerFactory(
            new InMemoryFriendRepository(),
            new InMemoryFriendRequestRepository(),
            List.of()
    );

    @Test
    void requestIfNotFriends() {
        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .isEmpty();

        UUID otherUser = UUID.randomUUID();
        friendManagerFactory.forUser(userId).tryFriend(otherUser);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .hasSize(1);
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .hasSize(1);
    }

    @Test
    void friendIfRequested() {
        UUID otherUser = UUID.randomUUID();
        friendManagerFactory.forUser(userId).tryFriend(otherUser);
        friendManagerFactory.forUser(otherUser).tryFriend(userId);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .hasSize(1);
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .isEmpty();
    }

    @Test
    void nothingIfFriends() {
        UUID otherUser = UUID.randomUUID();
        friendManagerFactory.forUser(userId).tryFriend(otherUser);
        friendManagerFactory.forUser(otherUser).tryFriend(userId);

        assertThat(friendManagerFactory.forUser(userId).tryFriend(otherUser))
                .isEqualByComparingTo(FriendManager.FriendResult.ALREADY_FRIENDS);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .hasSize(1);
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .isEmpty();
    }

    @Test
    void unfriendIfFriends() {
        UUID otherUser = UUID.randomUUID();
        friendManagerFactory.forUser(userId).tryFriend(otherUser);
        friendManagerFactory.forUser(otherUser).tryFriend(userId);

        friendManagerFactory.forUser(userId).tryUnfriend(otherUser);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .isEmpty();
    }

    @Test
    void unrequestIfRequested() {
        UUID otherUser = UUID.randomUUID();
        friendManagerFactory.forUser(userId).tryFriend(otherUser);
        friendManagerFactory.forUser(userId).tryUnfriend(otherUser);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .isEmpty();
    }

    @Test
    void nothingIfUnrequested() {
        UUID otherUser = UUID.randomUUID();
        assertThat(friendManagerFactory.forUser(userId).tryUnfriend(otherUser))
                .isEqualByComparingTo(FriendManager.UnfriendResult.NOT_FRIENDS_OR_REQUESTED);

        assertThat(friendManagerFactory.forUser(userId).getFriends())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(userId).getOutgoingRequests())
                .isEmpty();
        assertThat(friendManagerFactory.forUser(otherUser).getIncomingRequests())
                .isEmpty();
    }
}
