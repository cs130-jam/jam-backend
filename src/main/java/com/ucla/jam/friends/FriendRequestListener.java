package com.ucla.jam.friends;

import com.ucla.jam.user.User;

public interface FriendRequestListener {
    void friendRequested(User sourceUser, User targetUser);
}
