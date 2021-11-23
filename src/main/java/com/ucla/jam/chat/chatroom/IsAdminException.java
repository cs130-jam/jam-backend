package com.ucla.jam.chat.chatroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class IsAdminException extends RuntimeException {
    public IsAdminException() {
        super("User is chatroom admin");
    }
}
