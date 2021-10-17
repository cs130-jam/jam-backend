package com.ucla.jam.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucla.jam.UnknownTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

public class SessionTokenResolver implements HandlerMethodArgumentResolver {

    public static final String SESSION_TOKEN_KEY = "session-token";

    private final Key jwtKey;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Duration tokenTtl;

    public SessionTokenResolver(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.ttl}") Duration tokenTtl,
            ObjectMapper objectMapper,
            Clock clock
    ) {
        this.jwtKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.tokenTtl = tokenTtl;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public Optional<SessionInfo> fromToken(SessionToken token) {
        try {
            return Optional.of(objectMapper.readValue(
                    Jwts.parserBuilder()
                            .setSigningKey(jwtKey)
                            .setClock(() -> Date.from(clock.instant()))
                            .build()
                            .parseClaimsJws(token.getToken())
                            .getBody()
                            .getSubject(),
                    SessionInfo.class));
        } catch (JwtException | JsonProcessingException e) {
            return Optional.empty();
        }
    }

    @SneakyThrows(JsonProcessingException.class)
    public SessionToken toToken(SessionInfo sessionInfo) {
        return new SessionToken(Jwts.builder()
                .setSubject(objectMapper.writeValueAsString(sessionInfo))
                .setExpiration(Date.from(clock.instant().plus(tokenTtl)))
                .signWith(jwtKey)
                .compact());
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SessionFromHeader.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request, WebDataBinderFactory binderFactory) {
        if (!parameter.getParameterType().isAssignableFrom(SessionInfo.class)) {
            throw new IllegalArgumentException("@SessionFromHeader parameter must be assignable with SessionInfo object");
        }
        return Optional.ofNullable(request.getHeader(SESSION_TOKEN_KEY))
                .map(SessionToken::new)
                .flatMap(this::fromToken)
                .orElseThrow(UnknownTokenException::new);
    }
}
