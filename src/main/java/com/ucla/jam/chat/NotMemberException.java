package com.ucla.jam.chat;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NotMemberException extends RuntimeException {
    public NotMemberException() {
        super("Not a member of the given chatroom");
    }
}
