package com.ucla.jam.recommendation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Provides a configured web client for communication with recommendation server.
 */
@RequiredArgsConstructor
public class RecommendationWebClientProvider {

    @Getter
    private final String baseUrl;

    /**
     * Get configured web client.
     * @return Web client for communication with recommendation server.
     */
    @SneakyThrows
    public WebClient get() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
