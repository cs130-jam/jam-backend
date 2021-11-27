package com.ucla.jam.user.authentication;

import lombok.Value;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Internal credentials DTO for communication with database.
 * Represents user login credentials for users who create accounts with a username and password.
 */
@Value
public class InternalCredentials implements Credentials {
    String username;
    String passwordHash;

    @Override
    public Kind getKind() {
        return Kind.INTERNAL;
    }

    /**
     * Create internal credentials with given username and password.
     * Throws {@link InvalidSignupException} if the credentials are not valid.
     * @param username Login username
     * @param password Login password in plain text
     * @param salt Salt for hashing password
     * @return Internal credentials object
     */
    public static InternalCredentials internal(String username, String password, String salt) {
        if (username.length() == 0) {
            throw new InvalidSignupException("Username may not be empty");
        }
        if (password.length() < 8) {
            throw new InvalidSignupException("Password may not be less than 8 characters");
        }
        return new InternalCredentials(username, BCrypt.hashpw(password, salt));
    }

    public static InternalCredentials partialCredentials(String username) {
        return new InternalCredentials(username, "");
    }
}
