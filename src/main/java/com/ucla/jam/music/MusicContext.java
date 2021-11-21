package com.ucla.jam.music;

import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLException;

public class MusicContext {

    @Bean
    @SneakyThrows(SSLException.class)
    public DiscogsService discogsService(
            @Value("${discogs.api.base.url}") String baseUrl,
            @Value("${discogs.api.user.agent}") String userAgent,
            @Value("${discogs.api.user.token}") String token,
            @Value("${discogs.api.max.pagination.items}") int maxItems,
            @Value("${discogs.api.max.simultaneous.requests}") int batchSize
    ) {
        return new DiscogsService(
                new DiscogsWebClientProvider(
                        baseUrl,
                        userAgent,
                        token,
                        SslContextBuilder.forClient().build()),
                maxItems,
                batchSize
        );
    }
}
