package com.ucla.jam.music.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.util.List;

@Value
public class SearchResponse implements PaginatedResponse<SearchResponse.Result> {

    Pagination pagination;
    List<Result> results;

    @Override
    public List<Result> getItems() {
        return results;
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

        public static ArtistView ofResult(Result result, String baseUrl) {
            String trimmedUrl = result.getResource_url().replace(baseUrl, "");
            if (trimmedUrl.charAt(0) == '/') {
                trimmedUrl = trimmedUrl.substring(1);
            }
            return new ArtistView(result.getId(), result.getTitle(), result.getThumb(), trimmedUrl);
        }
    }
}
