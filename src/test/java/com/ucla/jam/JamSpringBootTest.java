package com.ucla.jam;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
@SpringBootTest(classes = JamContext.class)
@Testcontainers
public @interface JamSpringBootTest {
}
