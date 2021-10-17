package com.ucla.jam.user.authentication;

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
}
