package com.ucla.jam.notifications;

import com.ucla.jam.util.jooq.JsonConverter;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Notification {
    UUID id;
    UUID userId;
    String title;
    Action accept;
    Action reject;
    Instant at;

    @Value
    public static class Action {
        Method method;
        String url;

        public static class Converter extends JsonConverter<Action> {}
    }

    public enum Method {
        GET, POST, PUT, DELETE;
    }
}
