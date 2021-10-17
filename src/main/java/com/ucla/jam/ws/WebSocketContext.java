package com.ucla.jam.ws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

@Import({
        InMemorySessionRepository.class,
        SessionStorageSocketHandler.class
})
public class WebSocketContext {
    @Bean
    public WebSocketConfigurer webSocketConfigurer(
            SessionStorageSocketHandler socketHandler,
            @Value("${server.allowed.origin}") String allowedOrigin
    ) {
        return new WebSocketConfig(socketHandler, allowedOrigin);
    }
}
