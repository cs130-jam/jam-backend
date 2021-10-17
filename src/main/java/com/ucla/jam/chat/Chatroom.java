package com.ucla.jam.chat;

import com.google.common.collect.ImmutableList;
import com.ucla.jam.util.jooq.JsonConverter;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Value
public class Chatroom {
    @NonNull UUID id;
    ImmutableList<UUID> members;
    @With Instant updated;
    boolean isDirectMessage;
    @Nullable Info info;

    @Value
    public static class Info {
        String name;
        String topic;

        public static class Converter extends JsonConverter<Info> {}
    }
}
