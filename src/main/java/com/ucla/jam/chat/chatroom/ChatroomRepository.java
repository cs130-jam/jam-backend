package com.ucla.jam.chat.chatroom;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomRepository {

    /**
     * Get chat room with given id
     * @param roomId Room UUID
     * @return An empty optional if no chat room exists with given id.
     */
    Optional<Chatroom> get(UUID roomId);

    /**
     * Get all chat rooms which given user is a member of.
     * @param userId User UUID
     * @return List of chat rooms
     */
    Collection<Chatroom> getAll(UUID userId);

    /**
     * Remove given chat room.
     * Does nothing if chat room does not eixst.
     * @param roomId Chat room UUID
     */
    void remove(UUID roomId);

    /**
     * Insert chat room, or update chat room with same {@link Chatroom#id}
     * @param chatroom Chat room to insert, {@link Chatroom#members} field will be ignored and may be empty
     */
    void insert(Chatroom chatroom);

    /**
     * Remove given user from given chatroom. Does nothing if user is not a member of given chat room
     * @param roomId Room UUID
     * @param userId User UUID
     */
    void removeMember(UUID roomId, UUID userId);

    /**
     * Insert given user into given chat room. Does nothing if user is already a member the chat room
     * @param roomId Room UUID
     * @param userId User UUID
     */
    void insertMember(UUID roomId, UUID userId);
}
