package com.ucla.jam.resources;

import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.session.SessionFromHeader;
import com.ucla.jam.session.SessionInfo;
import com.ucla.jam.user.UnknownUserException;
import com.ucla.jam.user.User;
import com.ucla.jam.user.UserManager;
import com.ucla.jam.util.pagination.Pagination;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.ucla.jam.util.Futures.sneakyGet;
import static java.util.function.Predicate.not;
import static lombok.AccessLevel.PRIVATE;
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
        return new UserIdToken(sneakyGet(recommendationService.getRecommendation(
                user,
                new ValidUserPageHandler(
                        Set.copyOf(recommendationService.getVisited(user.getId())),
                        Set.copyOf(friendManagerFactory.forUser(user.getId()).getFriends()),
                        user)
                )));
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

    @RequiredArgsConstructor
    @AllArgsConstructor(access = PRIVATE)
    private class ValidUserPageHandler implements Pagination.PageHandler<UUID> {
        @With
        private UUID userId = null;
        private final Set<UUID> visitedUsers;
        private final Set<UUID> friends;
        private final User user;

        @Override
        public boolean isFinished() {
            return userId != null;
        }

        @Override
        public Pagination.PageHandler<UUID> handle(List<UUID> page) {
            return withUserId(page.stream()
                    .filter(not(visitedUsers::contains))
                    .filter(not(friends::contains))
                    .map(userManager::getUser)
                    .flatMap(Optional::stream)
                    .filter(user -> distance(user) < user.getPreferences().getMaxDistance().toMeters())
                    .filter(this::anyInstrumentMatches)
                    .findFirst()
                    .map(User::getId)
                    .orElse(null));

        }

        private long distance(User other) {
            return user.getProfile().getLocation().distance(other.getProfile().getLocation()).toMeters();
        }

        private boolean anyInstrumentMatches(User other) {
            if (user.getPreferences().getWantedInstruments().size() == 0) {
                return true;
            } else {
                return user.getPreferences().getWantedInstruments()
                        .stream()
                        .anyMatch(inst -> other.getProfile().getInstruments().contains(inst));
            }
        }

        @Override
        public List<UUID> getResult() {
            if (userId == null) {
                return List.of();
            } else {
                return List.of(userId);
            }
        }
    }
}
