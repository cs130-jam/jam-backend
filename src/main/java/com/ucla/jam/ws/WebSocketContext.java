package com.ucla.jam.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        InMemorySessionRepository.class,
        SessionStorageSocketHandler.class,
        WebSocketConfig.class
})
public class WebSocketContext {
    @Bean
    public WebSocketManager webSocketManager(InMemorySessionRepository sessionRepository) {
        return new WebSocketManager(sessionRepository);
    }
}
