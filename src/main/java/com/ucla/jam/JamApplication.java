package com.ucla.jam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
        JamContext.class
})
@SpringBootConfiguration
@EnableAutoConfiguration
public class JamApplication {

    public static void main(String[] args) {
        SpringApplication.run(JamApplication.class, args);
    }

}
