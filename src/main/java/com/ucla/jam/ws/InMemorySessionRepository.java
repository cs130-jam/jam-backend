package com.ucla.jam.ws;

import org.springframework.web.socket.WebSocketSession;

import javax.inject.Singleton;
import java.util.*;

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
