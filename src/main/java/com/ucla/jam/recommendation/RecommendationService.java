package com.ucla.jam.recommendation;

import com.ucla.jam.music.DiscogsService;
import com.ucla.jam.music.MusicInterest;
import com.ucla.jam.music.ResultHandler;
import com.ucla.jam.music.responses.MasterResourceResponse;
import com.ucla.jam.music.responses.Style;
import com.ucla.jam.recommendation.responses.GetRecommendationsResponse;
import com.ucla.jam.user.User;
import com.ucla.jam.util.Futures;
import com.ucla.jam.util.pagination.Pagination.PageHandler;
import com.ucla.jam.util.pagination.Pagination.PaginatedRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.ucla.jam.util.pagination.Pagination.paginatedRequest;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final VisitedRecommendationsRepository visitedRepository;
    private final RecommendationWebClientProvider webClientProvider;
    private final DiscogsService discogsService;
    private final int mastersSampleSize;

    public void triggerInsertUser(User user) {
        triggerInsertUser(user.getId(), user.getProfile());
    }

    public void updateUser(User oldUser, User.Profile newProfile) {
        Set<MusicInterest> oldInterests = Set.copyOf(oldUser.getProfile().getMusicInterests());
        Set<MusicInterest> newInterests = Set.copyOf(newProfile.getMusicInterests());
        if (!oldInterests.equals(newInterests)) {
            triggerInsertUser(oldUser.getId(), newProfile);
        }
    }

    private CompletableFuture<Void> triggerInsertUser(UUID userId, User.Profile profile) {
        CompletableFuture<Void> complete = new CompletableFuture<>();
        new Thread(() -> insertUser(userId, profile, complete)).start();
        return complete;
    }

    private void insertUser(UUID userId, User.Profile profile, CompletableFuture<Void> complete) {
        List<String> masterUrls = profile.getMusicInterests().stream()
                .map(MusicInterest::getName)
                .map(discogsService::artistMasterUrls)
                .map(Futures::sneakyGet)
                .map(urls -> urls.subList(0, Math.min(mastersSampleSize, urls.size())))
                .flatMap(Collection::stream)
                .collect(toList());
        log.info("Getting {} masters", masterUrls.size());
        Map<Style, Integer> stylesMap = masterUrls.stream()
                .map(discogsService::getMaster)
                .map(Futures::sneakyGet)
                .map(MasterResourceResponse::getStyles)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(toMap(identity(), style -> 1, Integer::sum));
        log.info("Got style map: {}", stylesMap);
        webClientProvider.get()
                .post()
                .uri("insert_user")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(new InsertRequestBody(userId, stylesMap)), InsertRequestBody.class)
                .retrieve()
                .onStatus(status -> !HttpStatus.NO_CONTENT.equals(status), clientResponse -> {
                    log.error("Failed to insert user to rec service, {}", clientResponse);
                    complete.completeExceptionally(new FailedInsertException());
                    return Mono.empty();
                })
                .toBodilessEntity()
                .subscribe(entity -> complete.complete(null));
    }

    public void markVisited(UUID sourceUser, UUID targetUser) {
        visitedRepository.markVisited(sourceUser, targetUser);
    }

    public List<UUID> getVisited(UUID userId) {
        return visitedRepository.getVisited(userId);
    }

    public Future<UUID> getRecommendation(User user, PageHandler<UUID> pageHandler) {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        ensureUser(user).thenAccept(success -> {
            if (success) {
                paginatedRequest(
                        new RecommendationRequest(user.getId()),
                        pageHandler,
                        new ResultHandler<>() {
                            @Override
                            public void completed(List<UUID> result) {
                                if (result.size() == 0) {
                                    future.completeExceptionally(new NoRecommendationFoundException());
                                } else {
                                    future.complete(result.get(0));
                                }
                            }

                            @Override
                            public void failed(Throwable error) {
                                future.completeExceptionally(error);
                            }
                        }
                );
            } else {
                future.completeExceptionally(new FailedInsertException());
            }
        });
        return future;
    }

    private CompletableFuture<Boolean> ensureUser(User user) {
        CompletableFuture<Boolean> exists = new CompletableFuture<>();
        ensureUser(user, true, exists);
        return exists;
    }

    private void ensureUser(User user, boolean tryInsert, CompletableFuture<Boolean> exists) {
        webClientProvider.get()
                .get()
                .uri(UriComponentsBuilder.fromUriString("user_exists")
                        .queryParam("uid", user.getId().toString())
                        .build()
                        .toUriString())
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NO_CONTENT), ignored -> {
                    exists.complete(true);
                    return Mono.empty();
                })
                .onStatus(status -> status.equals(HttpStatus.UNPROCESSABLE_ENTITY), ignored -> {
                    if (tryInsert) {
                        triggerInsertUser(user.getId(), user.getProfile())
                                .thenAccept(ignore -> ensureUser(user, false, exists))
                                .handle((_ignored, error) -> {
                                    log.error("Failed to ensure user, {}", error.getMessage());
                                    exists.complete(false);
                                    return null;
                                });
                    } else {
                        exists.complete(false);
                    }
                    return Mono.empty();
                })
                .toBodilessEntity()
                .doOnError(ignored -> exists.complete(false))
                .subscribe();
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
}
