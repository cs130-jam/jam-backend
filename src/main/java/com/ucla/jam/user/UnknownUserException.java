package com.ucla.jam.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnknownUserException extends RuntimeException {
    public UnknownUserException() {
        super("User with given id cannot be found");
    }
}
