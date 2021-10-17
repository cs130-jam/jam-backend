package com.ucla.jam.chat;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomRepository {
    Optional<Chatroom> get(UUID roomId);
    Collection<Chatroom> getAll(UUID userId);
    Collection<Chatroom> allChatrooms(); // temporary function

    void insert(Chatroom chatroom);
    void removeMember(UUID roomId, UUID userId);
    void insertMember(UUID roomId, UUID userId);
}
