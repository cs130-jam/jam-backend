package com.ucla.jam.chat;

import com.ucla.jam.ws.WebSocketContext;
import com.ucla.jam.ws.WebSocketManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        WebSocketContext.class,

        DbChatRepository.class,
        DbChatroomRepository.class,
        DbChatroomInviteRepository.class
})
public class ChatContext {
    @Bean
    public ChatManager chatManager(
            ChatroomRepository chatroomRepository,
            ChatroomInviteRepository inviteRepository,
            ChatRepository chatRepository,
            WebSocketManager webSocketManager,
            Clock clock
    ) {
        return new ChatManager(chatroomRepository, inviteRepository, chatRepository, webSocketManager, clock);
    }
}
