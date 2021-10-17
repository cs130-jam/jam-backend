package com.ucla.jam.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final String WS_URL = "/ws/jam";

    private final SessionStorageSocketHandler socketHandler;
    private final String allowedOrigin;

    public WebSocketConfig(
            SessionStorageSocketHandler socketHandler,
            @Value("${server.allowed.origin}") String allowedOrigin
    ) {
        this.socketHandler = socketHandler;
        this.allowedOrigin = allowedOrigin;
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, WS_URL)
                .setAllowedOrigins(allowedOrigin);
    }
}
