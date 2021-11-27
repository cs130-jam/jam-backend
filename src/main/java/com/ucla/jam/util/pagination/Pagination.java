package com.ucla.jam.util.pagination;

import com.ucla.jam.music.ResultHandler;
import lombok.RequiredArgsConstructor;
import lombok.With;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static lombok.AccessLevel.PRIVATE;

/**
 * Helper class for more easily performing pagination type requests over HTTP endpoints.
 */
public class Pagination {

    /**
     * Pagination where all records are accumulated into a single list.
     * @param request Request to perform
     * @param countRemaining Maximum number of records to return
     * @param handler Pagination handler
     * @param <I> Type of records
     * @param <T> Type of response from HTTP endpoint
     */
    public static <I, T extends PaginatedResponse<I>> void accumulatingPaginatedRequest(
            PaginatedRequest<I, T> request,
            int countRemaining,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, Accumulator.withLimit(countRemaining), 1, handler);
    }

    /**
     * Pagination where all records are accumulated into a single list.
     * @param request Request to perform
     * @param handler Pagination handler
     * @param <I> Type of records
     * @param <T> Type of response from HTTP endpoint
     */
    public static <I, T extends PaginatedResponse<I>> void accumulatingPaginatedRequest(
            PaginatedRequest<I, T> request,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, Accumulator.withoutLimit(), 1, handler);
    }

    /**
     * Generic handler for pagination, starting from first page.
     * @param request Request to perform
     * @param pageHandler Handler for each page returned
     * @param handler Pagination handler
     * @param <I> Type of records
     * @param <T> Type of response from HTTP endpoint
     */
    public static <I, T extends PaginatedResponse<I>> void paginatedRequest(
            PaginatedRequest<I, T> request,
            PageHandler<I> pageHandler,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, pageHandler, 1, handler);
    }

    /**
     * Generic handler for pagination.
     * @param request Request to perform
     * @param pageHandler Handler for each page returned
     * @param page Page index
     * @param handler Pagination handler
     * @param <I> Type of records
     * @param <T> Type of response from HTTP endpoint
     */
    public static <I, T extends PaginatedResponse<I>> void paginatedRequest(
            PaginatedRequest<I, T> request,
            PageHandler<I> pageHandler,
            int page,
            ResultHandler<List<I>> handler
    ) {
        request.withPage(page)
                .doOnError(handler::failed)
                .onErrorResume(error -> Mono.empty())
                .subscribe(response -> {
                    if (response.getPagination().getTotalPages() < page) {
                        handler.failed(new NoPagesRemainingException());
                        return;
                    }
                    PageHandler<I> nextHandler = pageHandler.handle(response.getItems());
                    if (page == response.getPagination().getTotalPages() || nextHandler.isFinished()) {
                        handler.completed(nextHandler.getResult());
                    } else {
                        paginatedRequest(request, nextHandler, page + 1, handler);
                    }
                });
    }

    /**
     * Request to pagination type HTTP endpoint
     * @param <I> Type of records in the pagination
     * @param <T> Type of response from the endpoint
     */
    public interface PaginatedRequest<I, T extends PaginatedResponse<I>> {
        /**
         * Get pagination for a particular page
         * @param page Page index
         * @return Mono which will contain pagination response
         */
        Mono<T> withPage(int page);
    }

    /**
     * Handler for each page returned from a pagination endpoint.
     * @param <I> Type of records in pagination
     */
    public interface PageHandler<I> {

        /**
         * Returns whether the page handler has all the pages it needs.
         * @return True if no more pages are needed, false otherwise
         */
        boolean isFinished();

        /**
         * Handle a given page.
         * @param page Page to handle
         * @return New PageHandler with relevant data from given page
         */
        PageHandler<I> handle(List<I> page);

        /**
         * Get the output of this page handler.
         * This method only needs to be valid even before {@link PageHandler#isFinished()} is true,
         * because the endpoint could run out of pages.
         * @return List of records
         */
        List<I> getResult();
    }

    @RequiredArgsConstructor(access = PRIVATE)
    private static class Accumulator<I> implements PageHandler<I> {
        private final int countLimit;
        @With
        private final List<I> items;

        @Override
        public boolean isFinished() {
            return !(countLimit == -1 || items.size() < countLimit);
        }

        @Override
        public PageHandler<I> handle(List<I> page) {
            int countRemaining = countLimit == -1 ? page.size() : countLimit - items.size();
            return withItems(Stream.concat(
                    items.stream(),
                            page.stream().limit(countRemaining))
                    .collect(toUnmodifiableList()));
        }

        @Override
        public List<I> getResult() {
            return items;
        }

        public static <I> Accumulator<I> withLimit(int maxCount) {
            return new Accumulator<>(maxCount, List.of());
        }

        public static <I> Accumulator<I> withoutLimit() {
            return new Accumulator<>(-1, List.of());
        }
    }
}
