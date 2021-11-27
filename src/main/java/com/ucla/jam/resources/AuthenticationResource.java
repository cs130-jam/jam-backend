package com.ucla.jam.resources;

import com.ucla.jam.music.MusicInterest;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.user.User;
import com.ucla.jam.user.User.Instrument;
import com.ucla.jam.user.UserManager;
import com.ucla.jam.user.authentication.AuthenticationManager;
import com.ucla.jam.user.authentication.ExistingUserException;
import com.ucla.jam.user.authentication.InternalCredentials;
import com.ucla.jam.user.authentication.InvalidCredentialsException;
import com.ucla.jam.util.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;
    private final String passwordSalt;

    @java.beans.ConstructorProperties({"authenticationManager", "userManager", "passwordSalt"})
    public AuthenticationResource(
            AuthenticationManager authenticationManager,
            UserManager userManager,
            @Value("${bcrypt.salt}") String passwordSalt
    ) {
        this.authenticationManager = authenticationManager;
        this.userManager = userManager;
        this.passwordSalt = passwordSalt;
    }

    @PostMapping(value = "internal/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalLogin(@RequestBody InternalUserInfo userInfo) {
        return authenticationManager.loginUser(InternalCredentials.internal(userInfo.getUsername(), userInfo.getPassword(), passwordSalt))
                .map(SessionToken::getToken)
                .map(UserTokenResponse::new)
                .orElseThrow(InvalidCredentialsException::new);
    }

    @PostMapping(value = "internal/signup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalSignup(@RequestBody InternalUserSignupInfo signupInfo) {
        if (authenticationManager.isUserExist(InternalCredentials.partialCredentials(signupInfo.username))) {
            throw new ExistingUserException();
        }
        InternalCredentials credentials = InternalCredentials.internal(signupInfo.getUsername(), signupInfo.getPassword(), passwordSalt);
        User newUser = userManager.addNewUser(signupInfo.toProfile());
        return new UserTokenResponse(authenticationManager.signupUser(newUser.getId(), credentials).getToken());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping(value = "internal/user")
    public void internalUserAvailable(@RequestParam String username) {
        if (authenticationManager.isUserExist(InternalCredentials.partialCredentials(username))) {
            throw new ExistingUserException();
        }
    }

    @lombok.Value
    private static class InternalUserInfo {
        String username;
        String password;
    }

    @lombok.Value
    private static class InternalUserSignupInfo {
        String username;
        String password;

        String firstName;
        String lastName;
        Location location;
        List<MusicInterest> musicInterests;
        List<Instrument> instruments;

        public User.Profile toProfile() {
            return new User.Profile(
                    firstName,
                    lastName,
                    "",
                    location,
                    "",
                    musicInterests,
                    instruments
            );
        }
    }

    @lombok.Value
    private static class UserTokenResponse {
        String token;
    }
}
