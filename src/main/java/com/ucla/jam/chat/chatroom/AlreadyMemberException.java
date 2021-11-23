package com.ucla.jam.chat.chatroom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyMemberException extends RuntimeException {
    public AlreadyMemberException() {
        super("Given user is already a member of given chatroom");
    }
}
