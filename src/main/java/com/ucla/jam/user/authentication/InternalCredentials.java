package com.ucla.jam.user.authentication;

import lombok.Value;

@Value
public class InternalCredentials implements Credentials {
    String username;
    String passwordHash;

    @Override
    public Kind getKind() {
        return Kind.INTERNAL;
    }
}
