package com.ucla.jam.chat.chatroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAdminException extends RuntimeException {
    public NotAdminException() {
        super("Current user is not the admin of given chatroom");
    }
}
