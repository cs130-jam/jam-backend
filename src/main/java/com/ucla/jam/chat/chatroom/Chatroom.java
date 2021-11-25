package com.ucla.jam.chat.chatroom;

import com.ucla.jam.util.jooq.JsonConverter;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Value
public class Chatroom {
    @NonNull UUID id;
    @With(PACKAGE) Set<UUID> members; // only use with here if you actually know what you're doing
    @With Instant updated;
    boolean isDirectMessage;
    @Nullable @With Info info;

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
