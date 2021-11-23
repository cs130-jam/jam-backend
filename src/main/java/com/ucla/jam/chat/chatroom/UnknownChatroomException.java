package com.ucla.jam.chat.chatroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnknownChatroomException extends RuntimeException {
    public UnknownChatroomException() {
        super("Chatroom cannot be found with given id");
    }
}
