package com.ucla.jam.ws;

import com.ucla.jam.UnknownTokenException;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@RequiredArgsConstructor
public class SessionStorageSocketHandler extends TextWebSocketHandler {

    private final SessionRepository sessionRepository;
    private final SessionTokenResolver tokenResolver;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        UUID userId = tokenResolver.fromToken(new SessionToken(message.getPayload()))
                .map(SessionInfo::getUserId)
                .orElseThrow(UnknownTokenException::new);
        sessionRepository.insert(userId, session);
    }
}
