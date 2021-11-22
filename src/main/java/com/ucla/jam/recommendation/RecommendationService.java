package com.ucla.jam.recommendation;

import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
import com.ucla.jam.music.DiscogsService;
import com.ucla.jam.music.MusicInterest;
import com.ucla.jam.music.ResultHandler;
import com.ucla.jam.music.responses.Style;
import com.ucla.jam.recommendation.responses.GetRecommendationsResponse;
import com.ucla.jam.user.User;
import com.ucla.jam.util.pagination.Pagination.PageHandler;
import com.ucla.jam.util.pagination.Pagination.PaginatedRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static com.ucla.jam.util.pagination.Pagination.paginatedRequest;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final VisitedRecommendationsRepository visitedRepository;
    private final FriendManagerFactory friendManagerFactory;
    private final RecommendationWebClientProvider webClientProvider;
    private final DiscogsService discogsService;

    public void insertUser(User user) {
        insertUser(user.getId(), user.getProfile());
    }

    public void updateUser(User oldUser, User.Profile newProfile) {
        Set<MusicInterest> oldInterests = Set.copyOf(oldUser.getProfile().getMusicInterests());
        Set<MusicInterest> newInterests = Set.copyOf(newProfile.getMusicInterests());
        if (!oldInterests.equals(newInterests)) {
            insertUser(oldUser.getId(), newProfile);
        }
    }

    private void insertUser(UUID userId, User.Profile profile) {
        CompletableFuture<Map<Style, Integer>> identity = CompletableFuture.completedFuture(new HashMap<>());
        profile.getMusicInterests().stream()
                .map(interest -> discogsService.artistStyles(interest.getPath()))
                .reduce(identity, (acc, stylesFuture) -> acc.thenCombine(stylesFuture, RecommendationService::combineStyleMaps))
                .thenApply(styleMap -> webClientProvider.get()
                        .post()
                        .uri("insert_user")
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(new InsertRequestBody(userId, styleMap)), InsertRequestBody.class)
                        .retrieve()
                        .onStatus(status -> !HttpStatus.NO_CONTENT.equals(status), clientResponse -> {
                            log.error("Failed to insert user to rec service, {}", clientResponse);
                            return Mono.empty();
                        })
                        .toBodilessEntity()
                        .subscribe(entity -> log.info("Got status code = {}", entity.getStatusCode())));
    }

    @SneakyThrows
    private static Map<Style, Integer> combineStyleMaps(Map<Style, Integer> mapA, Map<Style, Integer> mapB) {
        Thread.sleep(2000);
        return Stream.of(mapA, mapB)
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Entry::getKey, Entry::getValue, Integer::sum));
    }

    public void markVisited(UUID sourceUser, UUID targetUser) {
        visitedRepository.markVisited(sourceUser, targetUser);
    }

    public Future<UUID> getRecommendation(UUID userId) {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        paginatedRequest(
                new RecommendationRequest(userId),
                ValidUserPageHandler.forVisitedUsersAndFriends(
                        Set.copyOf(visitedRepository.getVisited(userId)),
                        Set.copyOf(friendManagerFactory.forUser(userId).getFriends())),
                new ResultHandler<>() {
                    @Override
                    public void completed(List<UUID> result) {
                        future.complete(result.get(0));
                    }

                    @Override
                    public void failed(Throwable error) {
                        future.completeExceptionally(error);
                    }
                }
        );
        return future;
    }

    @Value
    private static class InsertRequestBody {
        UUID uid;
        Map<Style, Integer> genres;
    }

    @Value
    private class RecommendationRequest implements PaginatedRequest<UUID, GetRecommendationsResponse> {
        UUID userId;

        public Mono<GetRecommendationsResponse> withPage(int page) {
            return webClientProvider.get()
                    .get()
                    .uri(UriComponentsBuilder.fromUriString("get_match")
                            .queryParam("page", page)
                            .queryParam("uid", userId.toString())
                            .build()
                            .toUriString())
                    .retrieve()
                    .bodyToMono(GetRecommendationsResponse.class);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class ValidUserPageHandler implements PageHandler<UUID> {
        @With
        private final UUID userId;
        private final Set<UUID> visitedUsers;
        private final Set<UUID> friends;

        @Override
        public boolean isFinished() {
            return userId != null;
        }

        @Override
        public PageHandler<UUID> handle(List<UUID> page) {
            return withUserId(page.stream()
                    .filter(not(visitedUsers::contains))
                    .filter(not(friends::contains))
                    .findFirst()
                    .orElse(null));
        }

        @Override
        public List<UUID> getResult() {
            if (userId == null) {
                throw new NoRecommendationFoundException();
            }
            return List.of(userId);
        }

        public static ValidUserPageHandler forVisitedUsersAndFriends(Set<UUID> visitedUsers, Set<UUID> friends) {
            return new ValidUserPageHandler(null, visitedUsers, friends);
        }
    }
}
