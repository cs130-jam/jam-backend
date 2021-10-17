package com.ucla.jam.util.jooq;

import org.jooq.Converter;

import java.time.Instant;

public class InstantConverter implements Converter<Long, Instant> {
    @Override
    public Instant from(Long x) {
        return x == null ? null : Instant.ofEpochMilli(x);
    }

    @Override
    public Long to(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }

    @Override
    public Class<Long> fromType() {
        return Long.class;
    }

    @Override
    public Class<Instant> toType() {
        return Instant.class;
    }
}
