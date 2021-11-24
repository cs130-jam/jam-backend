package com.ucla.jam.resources;

import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.UserManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.ucla.jam.util.Futures.sneakyGet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecommendationResource {

    private final RecommendationService recommendationService;
    private final FriendManagerFactory friendManagerFactory;
    private final UserManager userManager;

    @GetMapping(value = "match", produces = APPLICATION_JSON_VALUE)
    public UserIdToken getRec(@SessionFromHeader SessionInfo sessionInfo) {
        User user = userManager.getUser(sessionInfo.getUserId())
                .orElseThrow(UnknownUserException::new);
        return new UserIdToken(sneakyGet(recommendationService.getRecommendation(user)));
    }

    @PostMapping(value = "match/accept", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptMatch(@SessionFromHeader SessionInfo sessionInfo, @RequestBody UserIdToken token) {
        friendManagerFactory.forUser(sessionInfo.getUserId()).tryFriend(token.getUserId());
        recommendationService.markVisited(sessionInfo.getUserId(), token.getUserId());
    }

    @PostMapping(value = "match/reject", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectMatch(@SessionFromHeader SessionInfo sessionInfo, @RequestBody UserIdToken token) {
        recommendationService.markVisited(sessionInfo.getUserId(), token.getUserId());
    }

    @Value
    private static class UserIdToken {
        UUID userId;
    }
}
