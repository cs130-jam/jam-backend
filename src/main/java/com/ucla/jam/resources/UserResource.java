package com.ucla.jam.resources;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.User.UserView;
import com.ucla.jam.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserResource {

    private final UserRepository userRepository;

    @GetMapping(value = "user", produces = APPLICATION_JSON_VALUE)
    public User getSessionUser(@SessionFromHeader SessionInfo sessionInfo) {
        return userRepository.find(sessionInfo.getUserId())
                .orElseThrow(UnknownUserException::new);
    }

    @GetMapping(value = "user/{userId}", produces = APPLICATION_JSON_VALUE)
    public UserView getUser(@PathVariable UUID userId) {
        return UserView.ofUser(userRepository.find(userId)
                .orElseThrow(UnknownUserException::new));
    }

    @GetMapping(value = "/api/user/choices/instruments", produces = APPLICATION_JSON_VALUE)
    public List<String> instruments() {
        return Arrays.stream(User.Instrument.values())
                .sorted((t1, t2) -> t2.usageFrequency - t1.usageFrequency) // descending order
                .map(instrument -> instrument.name)
                .collect(toList());
    }
}
