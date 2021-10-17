package com.ucla.jam.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private static final String WS_URL = "/ws/jam";

    private final SessionStorageSocketHandler socketHandler;
    private final String allowedOrigin;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, WS_URL)
                .setAllowedOrigins(allowedOrigin);
    }
}
