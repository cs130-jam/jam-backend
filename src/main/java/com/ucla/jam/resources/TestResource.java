package com.ucla.jam.resources;

import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.ws.WebSocketManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestResource {

    private final WebSocketManager webSocketManager;
    private final SessionTokenResolver tokenResolver;

    @GetMapping(value = "test/user/random", produces = APPLICATION_JSON_VALUE)
    public SessionToken randomUser() {
        return tokenResolver.toToken(new SessionInfo(UUID.randomUUID()));
    }

    @PostMapping(value = "test/ws/ping")
    public void ping(@SessionFromHeader SessionInfo sessionInfo) {
        webSocketManager.sendMessage(sessionInfo.getUserId(), "I'M PINGING YOU :D");
    }
}
