package com.ucla.jam.music;

import com.ucla.jam.music.responses.ArtistReleaseResponse;
import com.ucla.jam.music.responses.MasterResourceResponse;
import com.ucla.jam.music.responses.SearchResponse;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import com.ucla.jam.resources.ArtistResource;
import com.ucla.jam.util.pagination.Pagination.PaginatedRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.ucla.jam.util.pagination.Pagination.accumulatingPaginatedRequest;
import static java.util.stream.Collectors.toList;

/**
 * Provides methods to query Discogs API in various ways.
 */
@RequiredArgsConstructor
public class DiscogsService {

    private static final String ARTIST_SEARCH_TYPE = "artist";
    private static final String MASTER_SEARCH_TYPE = "master";
    private final DiscogsWebClientProvider webClientProvider;
    private final int maxItemCount;

    /**
     * Search Discogs API for the given artist, querying the given page
     * @param artist Artist name query
     * @param page page index, at least one
     * @return Future for search response
     */
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

    /**
     * {@link DiscogsService#artistSearch(String, int)} for the first page.
     * @param artist Artist name query
     * @return Future for search response
     */
    public Future<ArtistResource.QueryResponse> artistSearch(String artist) {
        return artistSearch(artist, 1);
    }

    /**
     * Return all master urls for the artist with the given name, up to a maximum of {@link DiscogsService#maxItemCount}.
     * @param artistName Exact artist name
     * @return Future for list of master urls
     */
    public Future<List<String>> artistMasterUrls(String artistName) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        accumulatingPaginatedRequest(
                new SearchRequest(artistName, MASTER_SEARCH_TYPE),
                maxItemCount,
                new ResultHandler<>() {
                    @Override
                    public void completed(List<SearchResponse.Result> results) {
                        future.complete(results.stream()
                                .filter(searchResult -> searchResult.getTitle().toLowerCase().startsWith(artistName.toLowerCase()))
                                .map(SearchResponse.Result::getResource_url)
                                .collect(toList()));
                    }

                    @Override
                    public void failed(Throwable error) {
                        future.completeExceptionally(error);
                    }
                }
        );
        return future;
    }

    /**
     * Get master from Discogs APi with the given url
     * @param masterUrl Url to master resource
     * @return Future of master response
     */
    public Future<MasterResourceResponse> getMaster(String masterUrl) {
        CompletableFuture<MasterResourceResponse> future = new CompletableFuture<>();
        webClientProvider.get()
                .get()
                .uri(masterUrl)
                .retrieve()
                .bodyToMono(MasterResourceResponse.class)
                .onErrorResume(error -> {
                    future.completeExceptionally(error);
                    return Mono.empty();
                })
                .subscribe(future::complete);
        return future;
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
                            .queryParam("sort", "have,desc")
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
