package com.ucla.jam.ws;

import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {

    /**
     * Get WebSocketSession for given user
     * @param userId User UUID
     * @return Empty optional if there is not a session for the given user, Optional of the session otherwise
     */
    Optional<WebSocketSession> get(UUID userId);

    /**
     * Insert a WebSocketSession for given user.
     * Replaces the existing session if one exists.
     * @param userId User UUID
     * @param session Web socket session to insert
     */
    void insert(UUID userId, WebSocketSession session);
}
