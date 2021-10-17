package com.ucla.jam.ws;

import com.ucla.jam.user.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class WebSocketManager {

    private final SessionRepository sessionRepository;

    @SneakyThrows(IOException.class)
    public void sendMessage(UUID userId, String message) {
        sessionRepository.get(userId)
                .orElseThrow(UnknownUserException::new)
                .sendMessage(new TextMessage(message));
    }

    public boolean userConnected(UUID userId) {
        return sessionRepository.get(userId)
                .map(WebSocketSession::isOpen)
                .orElse(false);
    }
}
