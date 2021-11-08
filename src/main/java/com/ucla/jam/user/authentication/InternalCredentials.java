package com.ucla.jam.user.authentication;

import lombok.Value;
import org.mindrot.jbcrypt.BCrypt;

@Value
public class InternalCredentials implements Credentials {
    String username;
    String passwordHash;

    @Override
    public Kind getKind() {
        return Kind.INTERNAL;
    }

    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public static InternalCredentials partialCredentials(String username) {
        return new InternalCredentials(username, "");
    }
}
