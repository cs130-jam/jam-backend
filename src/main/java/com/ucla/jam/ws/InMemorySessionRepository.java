package com.ucla.jam.ws;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;

@Singleton
public class InMemorySessionRepository implements SessionRepository {

    private final Map<UUID, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<WebSocketSession> get(UUID userId) {
        return Optional.ofNullable(sessions.get(userId));
    }

    @Override
    public void insert(UUID userId, WebSocketSession session) {
        sessions.put(userId, session);
    }
}
