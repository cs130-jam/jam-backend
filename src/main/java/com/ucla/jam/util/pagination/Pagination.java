package com.ucla.jam.util.pagination;

import com.ucla.jam.music.ResultHandler;
import com.ucla.jam.recommendation.NoRecommendationFoundException;
import lombok.RequiredArgsConstructor;
import lombok.With;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;
import static lombok.AccessLevel.PRIVATE;

public class Pagination {
    public static <I, T extends PaginatedResponse<I>> void accumulatingPaginatedRequest(
            PaginatedRequest<I, T> request,
            int countRemaining,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, Accumulator.withLimit(countRemaining), 1, handler);
    }

    public static <I, T extends PaginatedResponse<I>> void accumulatingPaginatedRequest(
            PaginatedRequest<I, T> request,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, Accumulator.withoutLimit(), 1, handler);
    }

    public static <I, T extends PaginatedResponse<I>> void paginatedRequest(
            PaginatedRequest<I, T> request,
            PageHandler<I> pageHandler,
            ResultHandler<List<I>> handler
    ) {
        paginatedRequest(request, pageHandler, 1, handler);
    }

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
                        throw new NoRecommendationFoundException();
                    }
                    PageHandler<I> nextHandler = pageHandler.handle(response.getItems());
                    if (nextHandler.isFinished()) {
                        handler.completed(nextHandler.getResult());
                    } else if (page == response.getPagination().getTotalPages()) {
                        throw new NoRecommendationFoundException();
                    } else {
                        paginatedRequest(request, nextHandler, page + 1, handler);
                    }
                });
    }

    public interface PaginatedRequest<I, T extends PaginatedResponse<I>> {
        Mono<T> withPage(int page);
    }

    public interface PageHandler<I> {
        boolean isFinished();
        PageHandler<I> handle(List<I> page);
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
