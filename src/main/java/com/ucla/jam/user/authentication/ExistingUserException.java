package com.ucla.jam.user.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ExistingUserException extends RuntimeException {
    public ExistingUserException() {
        super("User already exists");
    }
}
