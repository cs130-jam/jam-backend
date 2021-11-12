package com.ucla.jam.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.ucla.jam.music.DiscogsService;
import com.ucla.jam.music.SearchException;
import com.ucla.jam.music.responses.SearchResponse.ArtistView;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArtistResource {

    private final DiscogsService discogsService;

    @GetMapping(value = "/artist/search", produces = APPLICATION_JSON_VALUE)
    public QueryResponse search(@RequestParam String artist, @RequestParam(required = false) Integer page) {
        try {
            if (page == null) {
                return discogsService.artistSearch(artist).get();
            } else {
                return discogsService.artistSearch(artist, page).get();
            }
        } catch (InterruptedException e) {
            log.error("Future interrupted for artist search {}, error: {}", artist, e.toString());
            throw new SearchException();
        } catch (ExecutionException e) {
            log.error("Future execution failed for artist search {}, error: {}", artist, e.toString());
            throw new SearchException();
        }
    }

    @Value
    public static class QueryResponse {
        int page;
        int totalPages;
        List<ArtistView> responses;
    }

}