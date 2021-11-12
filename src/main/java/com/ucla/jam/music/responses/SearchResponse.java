package com.ucla.jam.music.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
public class SearchResponse {

    Pagination pagination;
    List<Result> results;

    @Value
    public static class Pagination {
        int page;
        int per_page;
        int pages;
        int items;
        Urls urls;
    }

    @Value
    public static class Urls {
        String last;
        String next;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Value
    public static class Result {
        String title;
        String country;
        String uri;
        List<String> genre;
        String resource_url;
        String type;
        String id;
        String thumb;
        String cover_image;
    }

    @Value
    public static class ArtistView {
        String id;
        String name;
        String thumb;
        String path;

        public static ArtistView ofResult(Result result) {
            return new ArtistView(result.getId(), result.getTitle(), result.getThumb(), result.getResource_url());
        }
    }
}
