package com.ucla.jam.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucla.jam.chat.Chat;
import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.chat.chatroom.UnknownChatroomException;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RequiredArgsConstructor
@RestController
public class ChatResource {

    private final ObjectMapper objectMapper;
    private final ChatManager chatManager;
    private final Clock clock;

    @SneakyThrows
    @GetMapping(value = "/chatroom/{roomId}/after", produces = APPLICATION_JSON_VALUE)
    public List<Chat> chatsAfter(@PathVariable UUID roomId, @RequestParam String time, @SessionFromHeader SessionInfo sessionInfo) {
        Instant timestamp = objectMapper.readerFor(Instant.class).readValue(time);
        if (chatManager.hasChatroom(sessionInfo.getUserId(), roomId)) {
            return chatManager.getChatsAfter(roomId, timestamp);
        } else {
            throw new UnknownChatroomException();
        }
    }

    @GetMapping(value = "/chatroom/{roomId}/recent", produces = APPLICATION_JSON_VALUE)
    public List<Chat> recentChats(@PathVariable UUID roomId, @RequestParam int count, @SessionFromHeader SessionInfo sessionInfo) {
        if (chatManager.hasChatroom(sessionInfo.getUserId(), roomId)) {
            return chatManager.getRecentChats(roomId, count);
        } else {
            throw new UnknownChatroomException();
        }
    }

    @PostMapping(value = "/chatroom/{roomId}", consumes = TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void sendChat(@PathVariable UUID roomId, @RequestBody String chat, @SessionFromHeader SessionInfo sessionInfo) {
        chatManager.sendChat(new Chat(null, roomId, sessionInfo.getUserId(), chat, clock.instant()));
    }
}
