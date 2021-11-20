package com.ucla.jam.friends;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AlreadyFriendsException extends RuntimeException {
    public AlreadyFriendsException() {
        super("Current user is already friends with given user");
    }
}
