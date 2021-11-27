package com.ucla.jam.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class RecommendationWebClientProvider {

    @Getter
    private final String baseUrl;

    @SneakyThrows
    public WebClient get() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
