package com.ucla.jam.chat;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.UUID;

@Value
public class Chat {
    @Nullable Integer id;
    @NonNull UUID roomId;
    @NonNull UUID senderId;
    String message;
    Instant at;
}
