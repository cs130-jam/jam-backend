package com.ucla.jam.friends;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SameUserException extends RuntimeException {
    public SameUserException() {
        super("Current user and given user are the same");
    }
}
