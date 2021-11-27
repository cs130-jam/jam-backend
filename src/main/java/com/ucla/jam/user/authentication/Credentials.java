package com.ucla.jam.user.authentication;

public interface Credentials {
    /**
     * Get the kind of the credentials implementing subclass.
     * @return Credentials kind
     */
    Kind getKind();

    enum Kind {
        INTERNAL;
    }
}
