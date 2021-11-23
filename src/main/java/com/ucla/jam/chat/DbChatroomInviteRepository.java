package com.ucla.jam.chat;

import generated.jooq.tables.records.ChatroomInvitesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static generated.jooq.Tables.CHATROOM_INVITES;

@RequiredArgsConstructor
public class DbChatroomInviteRepository implements ChatroomInviteRepository {

    private final DSLContext context;

    @Override
    public List<UUID> getAll(UUID userId) {
        return context.selectFrom(CHATROOM_INVITES)
                .where(CHATROOM_INVITES.TARGET.eq(userId))
                .fetch(CHATROOM_INVITES.ROOM);
    }

    @Override
    public void inviteUser(UUID sourceId, UUID targetId, UUID roomId) {
        context.insertInto(CHATROOM_INVITES)
                .set(new ChatroomInvitesRecord(roomId, sourceId, targetId))
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void uninviteUser(UUID targetId, UUID roomId) {
        context.deleteFrom(CHATROOM_INVITES)
                .where(CHATROOM_INVITES.TARGET.eq(targetId)
                        .and(CHATROOM_INVITES.ROOM.eq(roomId)))
                .execute();
    }

    @Override
    public Optional<UUID> inviter(UUID userId, UUID roomId) {
        return context.selectFrom(CHATROOM_INVITES)
                .where(CHATROOM_INVITES.TARGET.eq(userId)
                        .and(CHATROOM_INVITES.ROOM.eq(roomId)))
                .fetchOptional(CHATROOM_INVITES.SOURCE);
    }
}
