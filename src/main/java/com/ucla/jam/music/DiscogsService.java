package com.ucla.jam.music;

import static java.util.stream.Collectors.toList;

import com.ucla.jam.music.responses.SearchResponse;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import com.ucla.jam.resources.ArtistResource;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class DiscogsService {

    private static final String ARTIST_SEARCH_TYPE = "artist";
    private final DiscogsWebClientProvider webClientProvider;
//    private final int globalMaxCount;
//    private final int defaultCount;

    public Future<ArtistResource.QueryResponse> artistSearch(String artist, int page) {
        CompletableFuture<ArtistResource.QueryResponse> future = new CompletableFuture<>();
        new SearchRequest(artist, ARTIST_SEARCH_TYPE)
                .withPage(page)
                .doOnError(future::completeExceptionally)
                .onErrorResume(error -> Mono.empty())
                .subscribe(response -> future.complete(new ArtistResource.QueryResponse(
                        page,
                        response.getPagination().getPages(),
                        response.getResults()
                                .stream()
                                .map(ArtistView::ofResult)
                                .collect(toList()))));
        return future;
    }

    public Future<ArtistResource.QueryResponse> artistSearch(String artist) {
        return artistSearch(artist, 1);
//        return artistSearch(artist, defaultCount);
    }

//    public Future<List<ArtistView>> artistSearch(String artist, int maxCount) {
//        CompletableFuture<List<ArtistView>> future = new CompletableFuture<>();
//        paginatedRequest(
//                new SearchRequest(artist, ARTIST_SEARCH_TYPE),
//                min(maxCount, globalMaxCount),
//                new ResultHandler<>() {
//                    @Override
//                    public void completed(List<SearchResponse.Result> result) {
//                        future.complete(result.stream()
//                                .map(ArtistView::ofResult)
//                                .collect(toList()));
//                    }
//
//                    @Override
//                    public void failed(Throwable error) {
//                        future.completeExceptionally(error);
//                    }
//                });
//        return future;
//    }
//
//    private void paginatedRequest(
//            SearchRequest request,
//            int countRemaining,
//            ResultHandler<List<SearchResponse.Result>> handler
//    ) {
//        paginatedRequest(request, countRemaining, 1, List.of(), handler);
//    }
//
//    private void paginatedRequest(
//            SearchRequest request,
//            int countRemaining,
//            int page,
//            List<SearchResponse.Result> currentResults,
//            ResultHandler<List<SearchResponse.Result>> handler
//    ) {
//        request.withPage(page)
//                .doOnError(handler::failed)
//                .onErrorResume(error -> Mono.empty())
//                .subscribe(response -> {
//                    int perPage = response.getPagination().getPer_page();
//                    List<SearchResponse.Result> combinedResults = Stream.concat(
//                                    currentResults.stream(),
//                                    response.getResults()
//                                            .stream()
//                                            .limit(countRemaining))
//                            .collect(toList());
//                    if (countRemaining <= perPage || page >= response.getPagination().getPages()) {
//                        handler.completed(combinedResults);
//                    } else {
//                        paginatedRequest(request, countRemaining - perPage, page + 1, combinedResults, handler);
//                    }
//                });
//    }

    @Value
    private class SearchRequest {
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
}
