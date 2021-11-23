package com.ucla.jam.chat;

import com.ucla.jam.util.jooq.JsonConverter;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Value
public class Chatroom {
    @NonNull UUID id;
    Set<UUID> members;
    @With Instant updated;
    boolean isDirectMessage;
    @Nullable @With
    Info info;

    @Value
    public static class Info {
        @With
        String name;
        @With
        String topic;
        String picUrl;
        UUID admin;

        public static class Converter extends JsonConverter<Info> {}
    }
}
