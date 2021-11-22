package com.ucla.jam.friends;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        DbFriendRepository.class,
        DbFriendRequestRepository.class
})
public class FriendContext {

    @Bean
    public FriendManager.FriendManagerFactory friendManagerFactory(
            FriendRepository friendRepository,
            FriendRequestRepository friendRequestRepository
    ) {
        return new FriendManager.FriendManagerFactory(friendRepository, friendRequestRepository);
    }
}
