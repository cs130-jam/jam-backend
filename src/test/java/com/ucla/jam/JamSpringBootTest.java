package com.ucla.jam;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@SpringBootTest(classes = JamContext.class)
@Testcontainers
public @interface JamSpringBootTest {
}
