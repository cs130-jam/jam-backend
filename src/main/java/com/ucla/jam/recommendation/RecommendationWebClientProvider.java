package com.ucla.jam.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class RecommendationWebClientProvider {

    @Getter
    private final String baseUrl;

    @SneakyThrows
    public WebClient get() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
                })
                .build();
    }
}
