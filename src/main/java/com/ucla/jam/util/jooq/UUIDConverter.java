package com.ucla.jam.util.jooq;

import org.jooq.Converter;

import java.util.UUID;

public class UUIDConverter implements Converter<String, UUID> {
    @Override
    public UUID from(String s) {
        return s == null ? null : UUID.fromString(s);
    }

    @Override
    public String to(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<UUID> toType() {
        return UUID.class;
    }
}
