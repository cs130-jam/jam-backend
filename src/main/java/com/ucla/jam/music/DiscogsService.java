package com.ucla.jam.music;

import com.ucla.jam.music.responses.ArtistReleaseResponse;
import com.ucla.jam.music.responses.MasterResourceResponse;
import com.ucla.jam.music.responses.SearchResponse;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import com.ucla.jam.music.responses.Style;
import com.ucla.jam.resources.ArtistResource;
import com.ucla.jam.util.pagination.Pagination;
import com.ucla.jam.util.pagination.Pagination.PaginatedRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.ucla.jam.music.responses.ArtistReleaseResponse.Type.MASTER;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DiscogsService {

    private static final String ARTIST_SEARCH_TYPE = "artist";
    private final DiscogsWebClientProvider webClientProvider;
    private final int maxItemCount;
    private final int batchSize;

    public Future<ArtistResource.QueryResponse> artistSearch(String artist, int page) {
        CompletableFuture<ArtistResource.QueryResponse> future = new CompletableFuture<>();
        new SearchRequest(artist, ARTIST_SEARCH_TYPE)
                .withPage(page)
                .doOnError(future::completeExceptionally)
                .onErrorResume(error -> Mono.empty())
                .subscribe(response -> future.complete(new ArtistResource.QueryResponse(
                        page,
                        response.getPagination().getPages(),
                        response.getItems()
                                .stream()
                                .map(result -> ArtistView.ofResult(result, webClientProvider.getBaseUrl()))
                                .collect(toList()))));
        return future;
    }

    public Future<ArtistResource.QueryResponse> artistSearch(String artist) {
        return artistSearch(artist, 1);
    }

    public CompletableFuture<Map<Style, Integer>> artistStyles(String artistUrl) {
        CompletableFuture<Map<Style, Integer>> future = new CompletableFuture<>();
        Pagination.accumulatingPaginatedRequest(
                new ArtistReleasesRequest(artistUrl),
                maxItemCount,
                new ResultHandler<>() {
                    @Override
                    public void completed(List<ArtistReleaseResponse.Release> releases) {
                        getStyles(releases.stream()
                                .filter(release -> release.getType() == MASTER)
                                .collect(toList()),
                                future);
                    }

                    @Override
                    public void failed(Throwable error) {
                        future.completeExceptionally(error);
                    }
                });
        return future;
    }

    private void getStyles(List<ArtistReleaseResponse.Release> releases, CompletableFuture<Map<Style, Integer>> future) {
        Collections.shuffle(releases);
        Flux.fromIterable(releases.subList(0, batchSize))
                .flatMapSequential(release -> webClientProvider.get()
                        .get()
                        .uri(release.getResource_url())
                        .retrieve()
                        .bodyToMono(MasterResourceResponse.class)
                        .onErrorResume(error -> {
                            log.error("Failed to fetch master resource, {}", error.getMessage());
                            return Mono.empty();
                        }))
                .collectList()
                .subscribe(masters -> {
                    Map<Style, Integer> stylesMap = new HashMap<>();
                    masters.stream()
                            .filter(master -> master.getStyles() != null)
                            .flatMap(master -> master.getStyles().stream())
                            .forEach(style -> stylesMap.put(style, stylesMap.getOrDefault(style, 0) + 1));
                    future.complete(stylesMap);
                });
    }

    @Value
    private class SearchRequest implements PaginatedRequest<SearchResponse.Result, SearchResponse> {
        String query;
        String type;

        public Mono<SearchResponse> withPage(int page) {
            return webClientProvider.get()
                    .get()
                    .uri(UriComponentsBuilder.fromUriString("database/search")
                            .queryParam("q", query)
                            .queryParam("type", type)
                            .queryParam("page", page)
                            .queryParam("per_page", 100)
                            .build()
                            .toUriString())
                    .retrieve()
                    .bodyToMono(SearchResponse.class);
        }
    }

    @Value
    private class ArtistReleasesRequest implements PaginatedRequest<ArtistReleaseResponse.Release, ArtistReleaseResponse> {
        String artistUrl;

        public Mono<ArtistReleaseResponse> withPage(int page) {
            return webClientProvider.get()
                    .get()
                    .uri(UriComponentsBuilder.fromUriString(artistUrl)
                            .pathSegment("releases")
                            .queryParam("page", page)
                            .queryParam("per_page", 100)
                            .build()
                            .toUriString())
                    .retrieve()
                    .bodyToMono(ArtistReleaseResponse.class);
        }
    }
}
