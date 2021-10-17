package com.ucla.jam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucla.jam.util.ObjectMapperProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class JamContext {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperProvider.get();
    }
}
