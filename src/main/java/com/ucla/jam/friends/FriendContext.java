package com.ucla.jam.friends;

import org.springframework.context.annotation.Import;

@Import({
        DbFriendRepository.class,
        DbFriendRequestRepository.class
})
public class FriendContext {

    public FriendManager.FriendManagerFactory friendManagerFactory(
            FriendRepository friendRepository,
            FriendRequestRepository friendRequestRepository
    ) {
        return new FriendManager.FriendManagerFactory(friendRepository, friendRequestRepository);
    }
}
