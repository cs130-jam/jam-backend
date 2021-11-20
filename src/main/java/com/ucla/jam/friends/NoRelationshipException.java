package com.ucla.jam.friends;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class NoRelationshipException extends RuntimeException {
    public NoRelationshipException() {
        super("No friend request exists from current user to given user, nor are the users friends");
    }
}
