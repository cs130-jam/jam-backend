package com.ucla.jam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucla.jam.chat.ChatContext;
import com.ucla.jam.resources.ChatResource;
import com.ucla.jam.resources.ChatroomResource;
import com.ucla.jam.resources.TestResource;
import com.ucla.jam.resources.UserResource;
import com.ucla.jam.session.SessionTokenResolver;
import com.ucla.jam.user.UserContext;
import com.ucla.jam.util.ObjectMapperProvider;
import com.ucla.jam.ws.WebSocketContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.util.List;

@Import({
        SessionTokenResolver.class,
        WebSocketContext.class,
        UserContext.class,
        ChatContext.class,

        ChatResource.class,
        ChatroomResource.class,
        UserResource.class,
        TestResource.class
})
public class JamContext {
    @Bean
    public WebMvcConfigurer corsConfigurer(
            SessionTokenResolver tokenResolver,
            @Value("${server.allowed.origin}") String allowedOrigin
    ) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(tokenResolver);
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(allowedOrigin);
            }
        };
    }
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperProvider.get();
    }
}
