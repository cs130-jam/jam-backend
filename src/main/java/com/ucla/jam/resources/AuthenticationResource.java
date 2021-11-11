package com.ucla.jam.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;

    @PostMapping(value = "internal/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalLogin(@RequestBody InternalUserInfo userInfo) {
        return authenticationManager.loginUser(authenticationManager.internal(userInfo.getUsername(), userInfo.getPassword()))
                .map(SessionToken::getToken)
                .map(UserTokenResponse::new)
                .orElseThrow(InvalidCredentialsException::new);
    }

    @PostMapping(value = "internal/signup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalSignup(@RequestBody InternalUserSignupInfo signupInfo) {
        if (authenticationManager.isUserExist(InternalCredentials.partialCredentials(signupInfo.username))) {
            throw new ExistingUserException();
        }
        InternalCredentials credentials = authenticationManager.internal(signupInfo.getUsername(), signupInfo.getPassword());
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

    @Value
    private static class InternalUserInfo {
        String username;
        String password;
    }

    @Value
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

    @Value
    private static class UserTokenResponse {
        String token;
    }
}
