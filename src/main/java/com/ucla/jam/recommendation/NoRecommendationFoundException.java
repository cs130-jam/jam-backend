package com.ucla.jam.recommendation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class NoRecommendationFoundException extends RuntimeException {
    public NoRecommendationFoundException() {
        super("No recommendation can be found for given user");
    }
}
