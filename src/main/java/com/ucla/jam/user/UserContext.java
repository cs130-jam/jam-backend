package com.ucla.jam.user;

import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.user.authentication.AggregatedCredentialsRepository;
import com.ucla.jam.user.authentication.AuthenticationContext;
import com.ucla.jam.user.authentication.AuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        AuthenticationContext.class,
        SessionTokenResolver.class,
        DbUserRepository.class
})
public class UserContext {
    @Bean
    public UserManager userManager(UserRepository userRepository) {
        return new UserManager(userRepository);
    }
}
