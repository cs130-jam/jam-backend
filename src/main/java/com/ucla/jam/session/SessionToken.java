package com.ucla.jam.session;

import lombok.Value;

/**
 * Session token DTO for request headers
 */
@Value
public class SessionToken {
    /**
     * Signed JWT
     */
    String token;
}
