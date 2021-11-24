package com.ucla.jam.resources;

import com.ucla.jam.music.DiscogsService;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ucla.jam.util.Futures.sneakyGet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArtistResource {

    private final DiscogsService discogsService;

    @GetMapping(value = "/artist/search", produces = APPLICATION_JSON_VALUE)
    public QueryResponse search(@RequestParam String artist, @RequestParam(required = false) Integer page) {
        if (page == null) {
            return sneakyGet(discogsService.artistSearch(artist));
        } else {
            return sneakyGet(discogsService.artistSearch(artist, page));
        }
    }

    @Value
    public static class QueryResponse {
        int page;
        int totalPages;
        List<ArtistView> responses;
    }

}