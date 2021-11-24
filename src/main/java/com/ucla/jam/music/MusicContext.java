package com.ucla.jam.music;

import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.net.ssl.SSLException;
import java.time.Clock;

public class MusicContext {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @SneakyThrows(SSLException.class)
    public DiscogsWebClientProvider webClientProvider(
            @Value("${discogs.api.base.url}") String baseUrl,
            @Value("${discogs.api.user.agent}") String userAgent,
            @Value("${discogs.api.user.token}") String token,
            Clock clock
    ) {
        return new DiscogsWebClientProvider(
                baseUrl,
                userAgent,
                token,
                SslContextBuilder.forClient().build(),
                clock);
    }

    @Bean
    public DiscogsService discogsService(
            DiscogsWebClientProvider webClientProvider,
            @Value("${discogs.api.max.pagination.items}") int maxItems
    ) {
        return new DiscogsService(webClientProvider, maxItems);
    }
}
