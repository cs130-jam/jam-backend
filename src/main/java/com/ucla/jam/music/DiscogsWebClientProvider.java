package com.ucla.jam.music;

import io.netty.handler.ssl.SslContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class DiscogsWebClientProvider {

    private final static String TOKEN_HEADER_KEY = "token";
    @Getter
    private final String baseUrl;
    private final String userAgent;
    private final String token;
    private final SslContext context;

    @SneakyThrows
    public WebClient get() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .secure(t -> t.sslContext(context))))
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
                    httpHeaders.add(USER_AGENT, userAgent);
                    httpHeaders.add(AUTHORIZATION, "Discogs " + TOKEN_HEADER_KEY + "=" + token);
                })
                .build();
    }
}
