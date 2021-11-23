package com.ucla.jam.resources;

import com.google.common.collect.ImmutableSet;
import com.ucla.jam.chat.ChatManager;
import com.ucla.jam.chat.Chatroom;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
public class ChatroomResource {

    private final ChatManager chatManager;

    @GetMapping(value = "/chatrooms", produces = APPLICATION_JSON_VALUE)
    public List<UUID> getUserChatrooms(@SessionFromHeader SessionInfo sessionInfo) {
        return chatManager.userChatrooms(sessionInfo.getUserId()).stream()
                .map(Chatroom::getId)
                .collect(toList());
    }

    @GetMapping(value = "/chatroom/{roomId}")
    public Chatroom getChatroom(@PathVariable UUID roomId, @SessionFromHeader SessionInfo sessionInfo) {
        return chatManager.getChatroomIfMember(sessionInfo.getUserId(), roomId);
    }

    @PutMapping(value = "/chatroom/{roomId}/info", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInfo(@PathVariable UUID roomId, @RequestBody ChatroomUpdateBody update, @SessionFromHeader SessionInfo sessionInfo) {
        Chatroom chatroom = chatManager.getChatroomIfMember(sessionInfo.getUserId(), roomId);
        if (chatroom.isDirectMessage() || chatroom.getInfo() == null) return;

        chatManager.updateChatroom(chatroom.withInfo(
                chatroom.getInfo()
                        .withName(update.getName())
                        .withTopic(update.getTopic())));
    }

    @PostMapping(value = "/chatroom", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ChatroomIdResponse createChatroom(@RequestBody ChatroomCreationBody chatroom, @SessionFromHeader SessionInfo sessionInfo) {
        return new ChatroomIdResponse(chatManager.createChatroom(
                Set.copyOf(chatroom.getMembers()),
                sessionInfo.getUserId(),
                chatroom.getInfo().getName()));
    }

    @PostMapping(value = "/chatroom/{roomId}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinChatroom(@PathVariable UUID roomId, @SessionFromHeader SessionInfo sessionInfo) {
        chatManager.joinChatroom(sessionInfo.getUserId(), roomId);
    }

    @PostMapping(value = "/chatroom/{roomId}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveChatroom(@PathVariable UUID roomId, @SessionFromHeader SessionInfo sessionInfo) {
        chatManager.leaveChatroom(sessionInfo.getUserId(), roomId);
    }

    @PutMapping(value = "/chatroom/{roomId}/invite/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inviteUser(@PathVariable UUID roomId, @PathVariable UUID userId, @SessionFromHeader SessionInfo sessionInfo) {
        chatManager.inviteToChatroom(roomId, sessionInfo.getUserId(), userId);
    }

    @DeleteMapping(value = "/chatroom/{roomId}/invite/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uninviteUser(@PathVariable UUID roomId, @PathVariable UUID userId, @SessionFromHeader SessionInfo sessionInfo) {
         chatManager.uninviteFromChatroom(roomId, sessionInfo.getUserId(), userId);
    }

    @DeleteMapping(value = "/chatroom/{roomId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable UUID roomId, @PathVariable UUID userId, @SessionFromHeader SessionInfo sessionInfo) {
        chatManager.removeFromChatroom(roomId, sessionInfo.getUserId(), userId);
    }

    @Value
    private static class ChatroomUpdateBody {
        String name;
        String topic;
    }

    @Value
    private static class ChatroomCreationBody {
        List<UUID> members;
        Info info;

        @Value
        private static class Info {
            String name;
        }
    }

    @Value
    private static class ChatroomIdResponse {
        UUID id;
    }
}
