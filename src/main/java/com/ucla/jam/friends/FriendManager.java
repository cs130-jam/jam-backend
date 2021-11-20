package com.ucla.jam.friends;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.ucla.jam.friends.FriendManager.FriendResult.*;
import static com.ucla.jam.friends.FriendManager.UnfriendResult.*;

@RequiredArgsConstructor
public class FriendManager {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UUID userId;

    public FriendResult tryFriend(UUID targetId) {
        if (friendRepository.getAll(userId).contains(targetId)) {
            return ALREADY_FRIENDS;
        }

        if (friendRequestRepository.isRequested(targetId, userId)) {
            friendRepository.friend(userId, targetId);
            friendRequestRepository.unrequest(userId, targetId);
            friendRequestRepository.unrequest(targetId, userId);
            return ACCEPTED;
        }

        friendRequestRepository.request(userId, targetId);
        return REQUESTED;
    }

    public UnfriendResult tryUnfriend(UUID targetId) {
        if (friendRepository.getAll(userId).contains(targetId)) {
            friendRepository.unfriend(userId, targetId);
            return UNFRIENDED;
        }

        if (friendRequestRepository.isRequested(userId, targetId)) {
            friendRequestRepository.unrequest(userId, targetId);
            return UNREQUESTED;
        }

        return NOT_FRIENDS_OR_REQUESTED;
    }

    public List<UUID> getFriends() {
        return friendRepository.getAll(userId);
    }

    public List<UUID> getIncomingRequests() {
        return friendRequestRepository.requestsTo(userId);
    }

    public List<UUID> getOutgoingRequests() {
        return friendRequestRepository.requestsFrom(userId);
    }

    public enum FriendResult {
        ACCEPTED, REQUESTED, ALREADY_FRIENDS
    }

    public enum UnfriendResult {
        UNFRIENDED, UNREQUESTED, NOT_FRIENDS_OR_REQUESTED
    }

    @RequiredArgsConstructor
    public static class FriendManagerFactory {
        private final FriendRepository friendRepository;
        private final FriendRequestRepository friendRequestRepository;

        public FriendManager forUser(UUID userId) {
            return new FriendManager(friendRepository, friendRequestRepository, userId);
        }
    }

}
