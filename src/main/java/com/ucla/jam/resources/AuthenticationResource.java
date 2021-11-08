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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;

    @PostMapping(value = "login/internal", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalLogin(@RequestBody InternalUserInfo userInfo) {
        return authenticationManager.loginUser(authenticationManager.internal(userInfo.getUsername(), userInfo.getPassword()))
                .map(SessionToken::getToken)
                .map(UserTokenResponse::new)
                .orElseThrow(InvalidCredentialsException::new);
    }

    @PostMapping(value = "signup/internal", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserTokenResponse internalSignup(@RequestBody InternalUserSignupInfo signupInfo) {
        if (authenticationManager.isUserExist(InternalCredentials.partialCredentials(signupInfo.username))) {
            throw new ExistingUserException();
        }
        return new UserTokenResponse(authenticationManager.signupUser(
                userManager.addNewUser(signupInfo.toProfile()).getId(),
                authenticationManager.internal(signupInfo.getUsername(), signupInfo.getPassword()))
                .getToken());
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
