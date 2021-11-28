package com.ucla.jam.friends;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static com.ucla.jam.friends.FriendManager.FriendResult.*;
import static com.ucla.jam.friends.FriendManager.UnfriendResult.*;

/**
 * Handles sending and rejecting friend requests.
 * Also handles accepting friend requests.
 */
@RequiredArgsConstructor
public class FriendManager {

    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final List<FriendRequestListener> friendRequestListeners;
    private final UUID userId;

    /**
     * Attempt to friend target user as {@link FriendManager#userId}
     * @param targetId Target user UUID
     * @return Result of the attempt to friend the target user
     */
    public FriendResult tryFriend(UUID targetId) {
        if (targetId.equals(userId)) {
            throw new SameUserException();
        }
        if (friendRepository.getAll(userId).contains(targetId)) {
            return ALREADY_FRIENDS;
        }

        if (friendRequestRepository.isRequested(targetId, userId)) {
            friendRepository.friend(userId, targetId);
            friendRequestRepository.unrequest(userId, targetId);
            friendRequestRepository.unrequest(targetId, userId);
            return ACCEPTED;
        }

        if (!friendRequestRepository.isRequested(userId, targetId)) {
            friendRequestListeners.forEach(listener -> listener.friendRequested(userId, targetId));
            friendRequestRepository.request(userId, targetId);
        }
        return REQUESTED;
    }

    /**
     * Attempt to unfriend target user as {@link FriendManager#userId}
     * @param targetId Target user UUID
     * @return Result of the attempt to unfriend the target user
     */
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

    /**
     * Cancel request for target user to be friends with {@link FriendManager#userId}
     * @param targetId Target user UUID
     */
    public void cancelRequest(UUID targetId) {
        friendRequestRepository.unrequest(userId, targetId);
    }

    /**
     * Get all users who are friends with {@link FriendManager#userId}
     * @return List of UUIDs of user's friends
     */
    public List<UUID> getFriends() {
        return friendRepository.getAll(userId);
    }

    /**
     * List users who have sent a friend request to {@link FriendManager#userId}
     * @return List of UUIDs of users
     */
    public List<UUID> getIncomingRequests() {
        return friendRequestRepository.requestsTo(userId);
    }

    /**
     * List users who have been sent a friend request from {@link FriendManager#userId}
     * @return List of UUIDs of users
     */
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
        private final List<FriendRequestListener> friendRequestListeners;

        /**
         * Instantiate a {@link FriendManager} for the given user
         * @param userId User UUID
         * @return New {@link FriendManager} object
         */
        public FriendManager forUser(UUID userId) {
            return new FriendManager(friendRepository, friendRequestRepository, friendRequestListeners, userId);
        }
    }

}
