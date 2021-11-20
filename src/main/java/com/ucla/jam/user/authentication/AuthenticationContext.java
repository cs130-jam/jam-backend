package com.ucla.jam.user.authentication;

import com.ucla.jam.session.SessionTokenResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Import({
        DbInternalCredentialsRepository.class
})
public class AuthenticationContext {
    @Bean
    public AggregatedCredentialsRepository credentialsRepository(
            DbInternalCredentialsRepository internalCredentialsRepository
    ) {
        return new AggregatedCredentialsRepository(Map.of(
                Credentials.Kind.INTERNAL,
                internalCredentialsRepository
        ));
    }

    @Bean
    public AuthenticationManager loginManager(
            AggregatedCredentialsRepository credentialsRepository,
            SessionTokenResolver tokenResolver,
            @Value("${bcrypt.salt}") String salt
    ) {
        return new AuthenticationManager(credentialsRepository, tokenResolver, salt);
    }
}
