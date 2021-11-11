package com.ucla.jam.user.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidSignupException extends RuntimeException {
    public InvalidSignupException(String message) {
        super(message);
    }
}
