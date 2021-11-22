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
        MusicContext.class,
        FriendContext.class
})
public class RecommendationContext {

    @Bean
    public RecommendationService recommendationService(
        VisitedRecommendationsRepository repository,
        FriendManagerFactory friendManagerFactory,
        @Value("${recommendations.base.url}") String baseUrl,
        DiscogsService discogsService
    ) {
        return new RecommendationService(repository, friendManagerFactory, new RecommendationWebClientProvider(baseUrl), discogsService);
    }
}
