package com.ucla.jam.music;

import com.ucla.jam.music.responses.*;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import com.ucla.jam.resources.ArtistResource;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DiscogsService {

    private static final String ARTIST_SEARCH_TYPE = "artist";
    private final DiscogsWebClientProvider webClientProvider;
    private final int globalMaxCount;
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

    public Future<Map<Style, Integer>> artistStyles(String artistUrl) {
        CompletableFuture<Map<Style, Integer>> future = new CompletableFuture<>();
        paginatedRequest(
                new ArtistReleasesRequest(artistUrl),
                globalMaxCount,
                new ResultHandler<>() {
                    @Override
                    public void completed(List<ArtistReleaseResponse.Release> releases) {
                        getStyles(releases, future);
                    }

                    @Override
                    public void failed(Throwable error) {
                        future.completeExceptionally(error);
                    }
                });
        return future;
    }

    private void getStyles(List<ArtistReleaseResponse.Release> releases, CompletableFuture<Map<Style, Integer>> future) {
        Flux.fromIterable(releases)
                .flatMapSequential(release -> webClientProvider.get()
                        .get()
                        .uri(release.getResource_url())
                        .retrieve()
                        .bodyToMono(MasterResourceResponse.class)
                        .onErrorResume(error -> Mono.empty()),
                        batchSize)
                .collectList()
                .subscribe(masters -> {
                    Map<Style, Integer> stylesMap = new HashMap<>();
                    masters.stream()
                            .flatMap(master -> master.getStyles().stream())
                            .forEach(style -> stylesMap.put(style, stylesMap.getOrDefault(style, 0) + 1));
                    future.complete(stylesMap);
                });
    }

    private static <I, T extends PaginatedResponse<I>> void paginatedRequest(
            PaginatedRequest<I, T> request,
            int countRemaining,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, countRemaining, 1, List.of(), handler);
    }

    private static <I, T extends PaginatedResponse<I>> void paginatedRequest(
            PaginatedRequest<I, T> request,
            int countRemaining,
            int page,
            List<I> currentItems,
            ResultHandler<List<I>> handler
    ) {
        request.withPage(page)
                .doOnError(handler::failed)
                .onErrorResume(error -> Mono.empty())
                .subscribe(response -> {
                    int perPage = response.getPagination().getPer_page();
                    List<I> combinedResults = Stream.concat(
                                    currentItems.stream(),
                                    response.getItems()
                                            .stream()
                                            .limit(countRemaining))
                            .collect(toList());
                    if (countRemaining <= perPage || page >= response.getPagination().getPages()) {
                        handler.completed(combinedResults);
                    } else {
                        paginatedRequest(request, countRemaining - perPage, page + 1, combinedResults, handler);
                    }
                });
    }

    private interface PaginatedRequest<I, T extends PaginatedResponse<I>> {
        Mono<T> withPage(int page);
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
                            .build()
                            .toUriString())
                    .retrieve()
                    .bodyToMono(ArtistReleaseResponse.class);
        }
    }
}
