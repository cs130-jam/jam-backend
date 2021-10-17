package com.ucla.jam.user.authentication;

public interface Credentials {
    Kind getKind();

    enum Kind {
        INTERNAL;
    }
}
