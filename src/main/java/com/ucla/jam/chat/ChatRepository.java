package com.ucla.jam.chat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChatRepository {

    /**
     * Get list of chats for a given room after a given time.
     * @param room Room UUID
     * @param after Instant to find chats after, exclusive
     * @return List of chats, ordered by most recent first
     */
    List<Chat> getAfter(UUID room, Instant after);

    /**
     * Get at most count chats for a given room.
     * @param room Room UUID
     * @param count Maximum number of chats to get, strictly greater than zero
     * @return List of chats, ordered by most recent first
     */
    List<Chat> getRecent(UUID room, int count);

    /**
     * Saves the given chat to the chat list.
     * @param chat chat {@link Chat#id id} may be left null
     */
    void save(Chat chat);
}
