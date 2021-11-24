package com.ucla.jam.recommendation;

import com.ucla.jam.friends.FriendContext;
import com.ucla.jam.friends.FriendManager.FriendManagerFactory;
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
        DiscogsService discogsService,
        @Value("${recommendations.masters.sample.size}") int mastersSampleSize
    ) {
        return new RecommendationService(repository, new RecommendationWebClientProvider(baseUrl), discogsService, mastersSampleSize);
    }
}
