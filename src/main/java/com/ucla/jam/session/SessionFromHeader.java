package com.ucla.jam.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Put this annotation on a {@link SessionInfo} parameter in a resource header to get the session info from the request headers.
 * If this annotation is included, and the request does not include a valid session token, {@link UnknownTokenException} will be thrown.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionFromHeader {
}
