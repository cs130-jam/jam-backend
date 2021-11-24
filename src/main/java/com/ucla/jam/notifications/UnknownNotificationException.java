package com.ucla.jam.notifications;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownNotificationException extends RuntimeException {
    public UnknownNotificationException() {
        super("Notification with given id cannot be found");
    }
}
