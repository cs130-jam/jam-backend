package com.ucla.jam.ws;

import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    Optional<WebSocketSession> get(UUID userId);
    void insert(UUID userId, WebSocketSession session);
}
