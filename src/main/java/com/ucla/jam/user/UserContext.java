package com.ucla.jam.user;

import com.ucla.jam.recommendation.RecommendationContext;
import com.ucla.jam.recommendation.RecommendationService;
import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.user.authentication.AuthenticationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        AuthenticationContext.class,
        SessionTokenResolver.class,
        DbUserRepository.class,
        RecommendationContext.class
})
public class UserContext {
    @Bean
    public UserManager userManager(
            UserRepository userRepository,
            RecommendationService recommendationService
    ) {
        return new UserManager(userRepository, recommendationService);
    }
}
