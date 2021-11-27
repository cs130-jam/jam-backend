package com.ucla.jam.music;

import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Provider for configured Discogs API web client.
 * Must be a singleton in order to handle rate limiting properly.
 */
@RequiredArgsConstructor
public class DiscogsWebClientProvider {

    private final static String TOKEN_HEADER_KEY = "token";
    @Getter
    private final String baseUrl;
    private final String userAgent;
    private final String token;
    private final SslContext context;
    private final Clock clock;

    private List<Instant> reqs = new ArrayList<>();
    private final int maxReqs = 60;
    private final Duration maxReqsTimespan = Duration.ofMinutes(2);

    /**
     * Retrieve a web client for communication with Discogs API preconfigured with relevant headers.
     * If the Discogs API is being queried too often, this method will block until there is time for
     * another request to not be rate limited.
     * @return Configured web client
     */
    @SneakyThrows
    public synchronized WebClient get() {
        Instant minuteAgo = clock.instant().minus(maxReqsTimespan);
        while (reqs.size() > 0 && earliestReq().isBefore(minuteAgo)) {
            removeEarliestReq();
        }
        if (reqs.size() >= maxReqs) {
            Thread.sleep(Duration.between(minuteAgo, reqs.get(maxReqs - 1)).toMillis() + 500);
        }
        insertReq(clock.instant());
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .secure(t -> t.sslContext(context))))
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
                    httpHeaders.add(USER_AGENT, userAgent);
                    httpHeaders.add(AUTHORIZATION, "Discogs " + TOKEN_HEADER_KEY + "=" + token);
                })
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(1000000)) // 1mb
                        .build())
                .build();
    }

    private Instant earliestReq() {
        return reqs.get(reqs.size() - 1);
    }

    private void removeEarliestReq() {
        reqs.remove(reqs.size() - 1);
    }

    private void insertReq(Instant instant) {
        reqs.add(0, instant);
    }
}
