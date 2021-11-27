package com.ucla.jam.ws;

import com.ucla.jam.user.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

/**
 * Manages sending messages to connected users.
 */
@RequiredArgsConstructor
public class WebSocketManager {

    private final SessionRepository sessionRepository;

    /**
     * Send a message to the given user via websocket.
     * Throws {@link UnknownUserException} if no session exists for given user.
     * @param userId User UUID
     * @param message The message to send
     */
    @SneakyThrows(IOException.class)
    public void sendMessage(UUID userId, String message) {
        sessionRepository.get(userId)
                .orElseThrow(UnknownUserException::new)
                .sendMessage(new TextMessage(message));
    }

    /**
     * Check whether the given user has an active web socket session.
     * @param userId User UUID
     * @return True if an activate session exists, false otherwise
     */
    public boolean userConnected(UUID userId) {
        return sessionRepository.get(userId)
                .map(WebSocketSession::isOpen)
                .orElse(false);
    }
}
