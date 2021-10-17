package com.ucla.jam.chat;

import static generated.jooq.tables.Chats.CHATS;
import static java.lang.Math.min;

import generated.jooq.tables.records.ChatsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DbChatRepository implements ChatRepository {

    private final DSLContext context;
    private final Integer maxChats;

    public DbChatRepository(
            DSLContext context,
            @Value("${db.chats.max}") Integer maxChats
    ) {
        this.context = context;
        this.maxChats = maxChats;
    }

    @Override
    public List<Chat> getAfter(UUID room, Instant after) {
        return context.selectFrom(CHATS)
                .where(CHATS.ROOM.eq(room)
                        .and(CHATS.AT.gt(after)))
                .orderBy(CHATS.AT.desc())
                .limit(maxChats)
                .fetch(this::fromRecord);
    }

    @Override
    public List<Chat> getRecent(UUID room, int count) {
        return context.selectFrom(CHATS)
                .where(CHATS.ROOM.eq(room))
                .orderBy(CHATS.AT.desc())
                .limit(min(count, maxChats))
                .fetch(this::fromRecord);
    }

    @Override
    public void save(Chat chat) {
        context.insertInto(CHATS,
                CHATS.ROOM, CHATS.USER, CHATS.MESSAGE, CHATS.AT)
                .values(chat.getRoomId(), chat.getSenderId(), chat.getMessage(), chat.getAt())
                .execute();
    }

    private Chat fromRecord(ChatsRecord record) {
        return new Chat(record.getId(), record.getRoom(), record.getUser(), record.getMessage(), record.getAt());
    }
}
