package com.ucla.jam.resources;

import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.chat.Chatroom;
import com.ucla.jam.chat.ChatroomRepository;
import com.ucla.jam.chat.UnknownChatroomException;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChatroomResource {

    private final ChatManager chatManager;
    private final ChatroomRepository chatroomRepository;
    private final Clock clock;

    @GetMapping(value = "/chatroom/{roomId}")
    public Chatroom getChatroom(@PathVariable UUID roomId, @SessionFromHeader SessionInfo sessionInfo) {
        if (chatManager.hasChatroom(sessionInfo.getUserId(), roomId)) {
            return chatManager.getChatroom(roomId)
                    .orElseThrow(UnknownChatroomException::new);
        } else {
            throw new UnknownChatroomException();
        }
    }
}
