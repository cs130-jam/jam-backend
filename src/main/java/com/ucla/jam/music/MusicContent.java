package com.ucla.jam.music;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLException;

public class MusicContent {

    @Bean
    @SneakyThrows(SSLException.class)
    public SslContext sslContext() {
        return SslContextBuilder.forClient().build();
    }

    @Bean
    public DiscogsWebClientProvider webClientProvider(
            @Value("${discogs.api.base.url}") String baseUrl,
            @Value("${discogs.api.user.agent}") String userAgent,
            @Value("${discogs.api.user.token}") String token,
            SslContext sslContext
    ) {
        return new DiscogsWebClientProvider(baseUrl, userAgent, token, sslContext);
    }

    @Bean
    public DiscogsService discogsService(DiscogsWebClientProvider clientProvider) {
        return new DiscogsService(clientProvider);
    }
}