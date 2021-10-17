package com.ucla.jam.chat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ChatRepository {
    List<Chat> getAfter(UUID room, Instant after);
    List<Chat> getRecent(UUID room, int count);
    void save(Chat chat);
}
