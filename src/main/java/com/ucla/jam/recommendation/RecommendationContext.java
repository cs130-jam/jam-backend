package com.ucla.jam.recommendation;

import com.ucla.jam.music.DiscogsService;
import com.ucla.jam.music.MusicContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        DbVisitedRecommendationsRepository.class,
        MusicContext.class
})
public class RecommendationContext {

    @Bean
    public RecommendationService recommendationService(
        VisitedRecommendationsRepository repository,
        @Value("${recommendations.base.url}") String baseUrl,
        DiscogsService discogsService
    ) {
        return new RecommendationService(repository, new RecommendationWebClientProvider(baseUrl), discogsService);
    }
}
