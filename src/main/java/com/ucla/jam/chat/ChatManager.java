package com.ucla.jam.chat;

import com.google.common.collect.ImmutableSet;
import com.ucla.jam.chat.chatroom.*;
import com.ucla.jam.ws.WebSocketManager;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Handles sending and getting chats.
 * Also handles chat room creation, invitation, and updates.
 */
@RequiredArgsConstructor
public class ChatManager {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomInviteRepository inviteRepository;
    private final ChatRepository chatRepository;
    private final WebSocketManager webSocketManager;
    private final List<GroupInviteListener> groupInviteListeners;
    private final Clock clock;
    private final String defaultGroupPicUrl = "https://static.thenounproject.com/png/58999-200.png";

    /**
     * Send given chat to chat room. Notifies all members of chatroom via websocket that a new chat has been sent.
     * Throws UnknownChatroomException if the given chat room does not exist
     * @param chat chat {@link Chat#id id} may be left null
     */
    public void sendChat(Chat chat) {
        Chatroom chatroom = getChatroomIfMember(chat.getSenderId(), chat.getRoomId());
        chatRepository.save(chat);
        chatroomRepository.insert(chatroom.withUpdated(clock.instant()));
        chatroom.getMembers()
                .stream()
                .filter(webSocketManager::userConnected)
                .forEach(userId -> webSocketManager.sendMessage(userId, chatroom.getId().toString()));
    }

    /**
     * Get list of chats for a given room after a given time.
     * @param room Room UUID
     * @param after Instant to find chats after, exclusive
     * @return List of chats, ordered by most recent first
     */
    public List<Chat> getChatsAfter(UUID room, Instant after) {
        return chatRepository.getAfter(room, after);
    }

    /**
     * Get at most count chats for a given room.
     * @param room Room UUID
     * @param count Maximum number of chats to retrieve
     * @return List of chats, ordered by most recent first
     */
    public List<Chat> getRecentChats(UUID room, int count) {
        return chatRepository.getRecent(room, count);
    }

    /**
     * Get all chat rooms which given user is a member of.
     * @param userId User UUID
     * @return Unordered list of chat rooms
     */
    public Collection<Chatroom> userChatrooms(UUID userId) {
        return chatroomRepository.getAll(userId);
    }

    /**
     * Get the given chat room.
     * Throws NotMemberException if the given user is not a member of given chat room
     * Throws UnknownChatroomException if the given chat room does not exist
     * @param userId User UUID
     * @param roomId Room UUID
     * @return chat room which given user is a member of
     */
    public Chatroom getChatroomIfMember(UUID userId, UUID roomId) {
        if (!hasChatroom(userId, roomId)) {
            throw new NotMemberException();
        }
        return chatroomRepository.get(roomId)
                .orElseThrow(UnknownChatroomException::new);
    }

    /**
     * Insert chat room, or update chat room with same id.
     * @param chatroom Chat room to insert, {@link Chatroom#members} field will be ignored and may be empty
     */
    public void updateChatroom(Chatroom chatroom) {
        chatroomRepository.insert(chatroom);
    }

    /**
     * Creates a new chat room with given parameters.
     * @param members UUIDs of members of chat room. If admin is not included in this set, it will be added
     * @param admin Admin UUID
     * @param name Name of chat room
     * @return UUID of created chat room
     */
    public UUID createChatroom(Set<UUID> members, UUID admin, String name) {
        UUID roomId = UUID.randomUUID();
        Chatroom chatroom = new Chatroom(
                roomId,
                ImmutableSet.<UUID>builder()
                        .addAll(members)
                        .add(admin)
                        .build(),
                clock.instant(),
                false,
                new Chatroom.Info(
                        name,
                        "",
                        defaultGroupPicUrl,
                        admin
                ));
        chatroomRepository.insert(chatroom);
        chatroom.getMembers().forEach(member -> chatroomRepository.insertMember(roomId, member));
        return roomId;
    }

    /**
     * Ensure that a direct message chat room exists between the two given users.
     * Will create a chat room if one does not exist already.
     * @param userA UUID of userA
     * @param userB UUID of userB
     * @return UUID of chat room
     */
    public UUID ensureDmChatroom(UUID userA, UUID userB) {
        return userChatrooms(userA).stream()
                .filter(Chatroom::isDirectMessage)
                .filter(chatroom -> chatroom.getMembers().contains(userB))
                .findAny()
                .map(Chatroom::getId)
                .orElseGet(() -> createDmChatroom(userA, userB));
    }

    private UUID createDmChatroom(UUID userA, UUID userB) {
        if (userA.equals(userB)) return null;
        UUID roomId = UUID.randomUUID();
        Chatroom chatroom = new Chatroom(
                roomId,
                Set.of(userA, userB),
                clock.instant(),
                true,
                null);
        chatroomRepository.insert(chatroom);
        chatroom.getMembers().forEach(member -> chatroomRepository.insertMember(roomId, member));
        return roomId;
    }

    /**
     * Given user will join given chat room.
     * Throws NoInviteException if the given user has not been invited to given chat room.
     * @param userId User UUID
     * @param roomId Room UUID
     */
    public void joinChatroom(UUID userId, UUID roomId) {
        if (!inviteRepository.getAll(userId).contains(roomId)) {
            throw new NoInviteException();
        }
        chatroomRepository.insertMember(roomId, userId);
        inviteRepository.uninviteUser(userId, roomId);
    }

    /**
     * Given user will be removed from given chatroom.
     * Throws NotMemberException if given user is not a member of given chat room.
     * Throws DirectChatroomException if given chat room is a direct message chat room.
     * Throws IsAdminException if the user to be removed is the chat room's admin
     * @param userId User UUID
     * @param roomId Room UUID
     */
    public void leaveChatroom(UUID userId, UUID roomId) {
        Chatroom chatroom = getChatroomIfMember(userId, roomId);
        if (chatroom.isDirectMessage() || chatroom.getInfo() == null) {
            throw new DirectChatroomException();
        }
        if (chatroom.getInfo().getAdmin().equals(userId)) {
            throw new IsAdminException();
        }
        chatroomRepository.removeMember(roomId, userId);
    }

    /**
     * Source user invites target user to given chat room.
     * Throws NotMemberException if source user is not a member of given chat room.
     * Throws DirectChatroomException if given chat room is a direct message chat room.
     * Throws AlreadyMemberException if target user is already a member of given chat room.
     * @param roomId Room UUID
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     */
    public void inviteToChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        Chatroom chatroom = getChatroomIfMember(sourceId, roomId);
        if (chatroom.isDirectMessage()) {
            throw new DirectChatroomException();
        }
        if (chatroom.getMembers().contains(targetId)) {
            throw new AlreadyMemberException();
        }
        groupInviteListeners.forEach(listener -> listener.invitedToGroup(sourceId, targetId, roomId));
        inviteRepository.inviteUser(sourceId, targetId, roomId);
    }

    /**
     * Source user removes invite target user's invite to given chat roon.
     * Throws NoInviteException if target user is not invited to given chat room.
     * Throws NoInviteException if source user did not originally invite target user to given chat rom
     * @param roomId Room UUID
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     */
    public void uninviteFromChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        if (!inviteRepository.inviter(targetId, roomId)
                .map(sourceId::equals)
                .orElse(false)
        ) {
            throw new NoInviteException();
        }
        inviteRepository.uninviteUser(targetId, roomId);
    }

    /**
     * Remove invite for given user and given chatroom.
     * Does nothing if no such invite exists.
     * @param roomId Room UUID
     * @param targetId Target user UUID
     */
    public void rejectInvite(UUID roomId, UUID targetId) {
        inviteRepository.uninviteUser(targetId, roomId);
    }

    /**
     * Remove the target user from the given chat room.
     * Throws NotAdminException if source user is not admin of given chat room.
     * Triggers {@link #leaveChatroom(UUID, UUID) leaveChatroom}.
     * @param roomId Room UUID
     * @param sourceId Source user UUID
     * @param targetId Target user UUID
     */
    public void removeFromChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        Chatroom chatroom = getChatroomIfMember(sourceId, roomId);
        if (chatroom.getInfo() != null && !chatroom.getInfo().getAdmin().equals(sourceId)) {
            throw new NotAdminException();
        }
        leaveChatroom(targetId, roomId);
    }

    /**
     * Checks if the given user is a member of the given chat room.
     * @param userId User UUID
     * @param roomId Room UUID
     * @return Returns true if the user is a member of the given chat room, and false otherwise
     */
    public boolean hasChatroom(UUID userId, UUID roomId) {
        return chatroomRepository.get(roomId)
                .map(Chatroom::getMembers)
                .map(members -> members.contains(userId))
                .orElse(false);
    }
}
