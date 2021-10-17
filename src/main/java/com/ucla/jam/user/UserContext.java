package com.ucla.jam.user;

import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.user.authentication.AggregatedCredentialsRepository;
import com.ucla.jam.user.authentication.AuthenticationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        AuthenticationContext.class,
        SessionTokenResolver.class
})
public class UserContext {
    @Bean
    public LoginManager loginManager(
            AggregatedCredentialsRepository credentialsRepository,
            SessionTokenResolver tokenResolver
    ) {
        return new LoginManager(credentialsRepository, tokenResolver);
    }
}
