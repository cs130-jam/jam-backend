package com.ucla.jam.music.responses;

import lombok.Value;

@Value
public class Pagination {

    int page;
    int per_page;
    int pages;
    int items;
    Urls urls;

    @Value
    public static class Urls {
        String last;
        String next;
    }
}
