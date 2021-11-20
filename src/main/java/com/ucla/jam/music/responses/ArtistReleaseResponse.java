package com.ucla.jam.music.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
public class ArtistReleaseResponse implements PaginatedResponse<ArtistReleaseResponse.Release> {

    Pagination pagination;
    List<Release> releases;

    @Override
    public List<Release> getItems() {
        return releases;
    }

    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Release {
        String artist;
        String resource_url;
        String type;
    }

    @RequiredArgsConstructor
    public enum Type {
        MASTER("master"),
        RELEASE("release");

        @JsonValue
        public final String name;
    }
}
