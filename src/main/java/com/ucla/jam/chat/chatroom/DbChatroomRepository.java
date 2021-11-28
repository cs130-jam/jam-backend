package com.ucla.jam.chat.chatroom;

import generated.jooq.tables.records.ChatroomMembersRecord;
import generated.jooq.tables.records.ChatroomsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.*;

import static generated.jooq.tables.ChatroomMembers.CHATROOM_MEMBERS;
import static generated.jooq.tables.Chatrooms.CHATROOMS;
import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class DbChatroomRepository implements ChatroomRepository {

    private final DSLContext context;

    @Override
    public Optional<Chatroom> get(UUID roomId) {
        List<UUID> chatroomMembers = context.selectFrom(CHATROOM_MEMBERS)
                .where(CHATROOM_MEMBERS.ROOM.eq(roomId))
                .fetch(ChatroomMembersRecord::getUser);
        return context.selectFrom(CHATROOMS)
                .where(CHATROOMS.ID.eq(roomId))
                .fetchOptional(this::partialChatroomFromRecord)
                .map(chatroom -> chatroom.withMembers(Set.copyOf(chatroomMembers)));
    }

    @Override
    public Collection<Chatroom> getAll(UUID userId) {
        Map<UUID, List<ChatroomMembersRecord>> chatroomMembers = context.selectFrom(CHATROOM_MEMBERS)
                .where(CHATROOM_MEMBERS.ROOM.in(
                    context.selectFrom(CHATROOM_MEMBERS)
                            .where(CHATROOM_MEMBERS.USER.eq(userId))
                            .fetch(CHATROOM_MEMBERS.ROOM)
                ))
                .fetch()
                .stream()
                .collect(groupingBy(ChatroomMembersRecord::getRoom));
        return context.select(CHATROOM_MEMBERS.USER, CHATROOMS.asterisk())
                .from(CHATROOM_MEMBERS.leftJoin(CHATROOMS)
                        .on(CHATROOM_MEMBERS.ROOM.eq(CHATROOMS.ID)))
                .where(CHATROOM_MEMBERS.USER.eq(userId))
                .fetch(this::partialChatroomFromRecord)
                .stream()
                .map(chatroom -> chatroom.withMembers(chatroomMembers.get(chatroom.getId())
                        .stream()
                        .map(ChatroomMembersRecord::getUser)
                        .collect(toSet())))
                .collect(toList());
    }

    @Override
    public void remove(UUID roomId) {
        context.deleteFrom(CHATROOMS)
                .where(CHATROOMS.ID.eq(roomId))
                .execute();
    }

    @Override
    public void insert(Chatroom chatroom) {
        context.insertInto(CHATROOMS)
                .set(toRecord(chatroom))
                .onDuplicateKeyUpdate()
                .set(toRecord(chatroom))
                .execute();
    }

    @Override
    public void removeMember(UUID roomId, UUID userId) {
        context.deleteFrom(CHATROOM_MEMBERS)
                .where(CHATROOM_MEMBERS.ROOM.eq(roomId)
                        .and(CHATROOM_MEMBERS.USER.eq(userId)))
                .execute();
    }

    @Override
    public void insertMember(UUID roomId, UUID userId) {
        context.insertInto(CHATROOM_MEMBERS)
                .set(toMemberRecord(roomId, userId))
                .onDuplicateKeyIgnore()
                .execute();
    }

    private ChatroomsRecord toRecord(Chatroom chatroom) {
        return new ChatroomsRecord(chatroom.getId(), chatroom.getUpdated(), chatroom.isDirectMessage(), chatroom.getInfo());
    }

    private ChatroomMembersRecord toMemberRecord(UUID roomId, UUID userId) {
        return new ChatroomMembersRecord(roomId, userId);
    }

    private Chatroom partialChatroomFromRecord(org.jooq.Record record) {
        return new Chatroom(
                record.get(CHATROOMS.ID),
                Set.of(),
                record.get(CHATROOMS.UPDATED),
                record.get(CHATROOMS.ISDIRECTMESSAGE),
                record.get(CHATROOMS.INFO));
    }
}
