package com.ucla.jam.music.responses;

import com.ucla.jam.util.pagination.PaginationContext;
import lombok.Value;

@Value
public class DiscogsPagination implements PaginationContext {

    int page;
    int per_page;
    int pages;
    int items;
    Urls urls;

    @Override
    public int getTotalPages() {
        return pages;
    }

    @Value
    public static class Urls {
        String last;
        String next;
    }
}
