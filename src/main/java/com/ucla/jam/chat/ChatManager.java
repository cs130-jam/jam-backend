package com.ucla.jam.chat;

import com.google.common.collect.ImmutableSet;
import com.ucla.jam.chat.chatroom.*;
import com.ucla.jam.ws.WebSocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ChatManager {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomInviteRepository inviteRepository;
    private final ChatRepository chatRepository;
    private final WebSocketManager webSocketManager;
    private final List<GroupInviteListener> groupInviteListeners;
    private final Clock clock;
    private final String defaultGroupPicUrl = "https://static.thenounproject.com/png/58999-200.png";

    public void sendChat(Chat chat) {
        Chatroom chatroom = chatroomRepository.get(chat.getRoomId())
                        .orElseThrow(UnknownChatroomException::new);
        chatRepository.save(chat);
        chatroomRepository.insert(chatroom.withUpdated(clock.instant()));
        chatroom.getMembers()
                .stream()
                .filter(webSocketManager::userConnected)
                .forEach(userId -> webSocketManager.sendMessage(userId, chatroom.getId().toString()));
    }

    public List<Chat> getChatsAfter(UUID room, Instant after) {
        return chatRepository.getAfter(room, after);
    }

    public List<Chat> getRecentChats(UUID room, int count) {
        return chatRepository.getRecent(room, count);
    }

    public Collection<Chatroom> userChatrooms(UUID userId) {
        return chatroomRepository.getAll(userId);
    }

    public Chatroom getChatroomIfMember(UUID userId, UUID roomId) {
        if (!hasChatroom(userId, roomId)) {
            throw new NotMemberException();
        }
        return chatroomRepository.get(roomId)
                .orElseThrow(UnknownChatroomException::new);
    }

    public void updateChatroom(Chatroom chatroom) {
        chatroomRepository.insert(chatroom);
    }

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

    public UUID ensureDmChatroom(UUID userA, UUID userB) {
        return userChatrooms(userA).stream()
                .peek(room -> log.info("room {}", room))
                .filter(Chatroom::isDirectMessage)
                .filter(chatroom -> chatroom.getMembers().contains(userB))
                .findAny()
                .map(Chatroom::getId)
                .orElseGet(() -> createDmChatroom(userA, userB));
    }

    private UUID createDmChatroom(UUID userA, UUID userB) {
        log.info("creating for {} and {}", userA, userB);
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

    public void joinChatroom(UUID userId, UUID roomId) {
        if (!inviteRepository.getAll(userId).contains(roomId)) {
            throw new NoInviteException();
        }
        chatroomRepository.insertMember(roomId, userId);
        inviteRepository.uninviteUser(userId, roomId);
    }

    public void leaveChatroom(UUID userId, UUID roomId) {
        if (!hasChatroom(userId, roomId)) {
            throw new NotMemberException();
        }
        Chatroom chatroom = getChatroomIfMember(userId, roomId);
        if (chatroom.isDirectMessage() || chatroom.getInfo() == null) {
            throw new DirectChatroomException();
        }
        if (chatroom.getInfo().getAdmin().equals(userId)) {
            throw new IsAdminException();
        }
        chatroomRepository.removeMember(roomId, userId);
    }

    public void inviteToChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        Chatroom chatroom = chatroomRepository.get(roomId)
                .orElseThrow(UnknownChatroomException::new);
        if (chatroom.isDirectMessage()) {
            throw new DirectChatroomException();
        }
        if (chatroom.getMembers().contains(targetId)) {
            throw new AlreadyMemberException();
        }
        if (!chatroom.getMembers().contains(sourceId)) {
            throw new NotMemberException();
        }
        groupInviteListeners.forEach(listener -> listener.invitedToGroup(sourceId, targetId, roomId));
        inviteRepository.inviteUser(sourceId, targetId, roomId);
    }

    public void uninviteFromChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        if (!inviteRepository.inviter(targetId, roomId)
                .map(sourceId::equals)
                .orElse(false)
        ) {
            throw new NoInviteException();
        }
        inviteRepository.uninviteUser(targetId, roomId);
    }

    public void rejectInvite(UUID roomId, UUID targetId) {
        inviteRepository.uninviteUser(targetId, roomId);
    }

    public void removeFromChatroom(UUID roomId, UUID sourceId, UUID targetId) {
        Chatroom chatroom = getChatroomIfMember(sourceId, roomId);
        if (chatroom.isDirectMessage() || chatroom.getInfo() == null) {
            throw new DirectChatroomException();
        }
        if (!chatroom.getInfo().getAdmin().equals(sourceId)) {
            throw new NotAdminException();
        }
        leaveChatroom(targetId, roomId);
    }

    public boolean hasChatroom(UUID userId, UUID roomId) {
        return chatroomRepository.get(roomId)
                .map(Chatroom::getMembers)
                .map(members -> members.contains(userId))
                .orElse(false);
    }
}
