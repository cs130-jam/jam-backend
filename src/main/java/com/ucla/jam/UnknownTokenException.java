package com.ucla.jam;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnknownTokenException extends RuntimeException {
    public UnknownTokenException() {
        super("Invalid or missing session token");
    }
}
