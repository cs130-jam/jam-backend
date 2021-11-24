package com.ucla.jam.recommendation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedInsertException extends RuntimeException {
    public FailedInsertException() {
        super("Failed to insert user into rec service");
    }
}
