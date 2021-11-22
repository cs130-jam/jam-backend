package com.ucla.jam.resources;

import com.ucla.jam.friends.FriendManager;
import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.music.SearchException;
import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecommendationResource {

    private final RecommendationService recommendationService;
    private final FriendManagerFactory friendManagerFactory;

    @GetMapping(value = "match", produces = APPLICATION_JSON_VALUE)
    public UserIdToken getRec(@SessionFromHeader SessionInfo sessionInfo) {
        try {
            return new UserIdToken(recommendationService.getRecommendation(sessionInfo.getUserId()).get());
        } catch (InterruptedException e) {
            log.error("Future interrupted for user recommendation {}, error: {}", sessionInfo.getUserId(), e.toString());
            throw new SearchException();
        } catch (ExecutionException e) {
            log.error("Future execution failed for user recommendation{}, error: {}", sessionInfo.getUserId(), e.toString());
            throw new SearchException();
        }
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
