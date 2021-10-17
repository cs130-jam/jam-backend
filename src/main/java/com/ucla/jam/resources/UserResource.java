package com.ucla.jam.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.session.SessionToken;
import com.ucla.jam.session.SessionTokenResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserResource {

    private final SessionTokenResolver tokenResolver;

    @GetMapping(value = "/user/random", produces = APPLICATION_JSON_VALUE)
    public SessionToken randomUser() {
        UUID randomUserId = UUID.randomUUID();
        log.info("Return token for random UUID {}", randomUserId);
        return tokenResolver.toToken(new SessionInfo(randomUserId));
    }

    @PostMapping(value = "/user/test")
    @ResponseStatus(HttpStatus.OK)
    public void testUser(@SessionFromHeader SessionInfo sessionInfo) {
        log.info("Got user with UUID {}", sessionInfo.getUserId());
    }
}
