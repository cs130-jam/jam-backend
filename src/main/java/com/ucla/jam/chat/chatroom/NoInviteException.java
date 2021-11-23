package com.ucla.jam.chat.chatroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoInviteException extends RuntimeException {
    public NoInviteException() {
        super("Not invited to given chatroom");
    }
}
