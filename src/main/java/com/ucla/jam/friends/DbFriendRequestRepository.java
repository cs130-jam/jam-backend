package com.ucla.jam.friends;

import static generated.jooq.Tables.FRIEND_REQUESTS;

import generated.jooq.tables.records.FriendRequestsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DbFriendRequestRepository implements FriendRequestRepository {

    private final DSLContext context;

    @Override
    public void request(UUID sourceUser, UUID targetUser) {
        context.insertInto(FRIEND_REQUESTS)
                .set(toRecord(sourceUser, targetUser))
                .execute();
    }

    @Override
    public void unrequest(UUID sourceUser, UUID targetUser) {
        context.deleteFrom(FRIEND_REQUESTS)
                .where(FRIEND_REQUESTS.SOURCE.eq(sourceUser)
                        .and(FRIEND_REQUESTS.TARGET.eq(targetUser)))
                .execute();
    }

    @Override
    public boolean isRequested(UUID sourceUser, UUID targetUser) {
        return context.selectFrom(FRIEND_REQUESTS)
                .where(FRIEND_REQUESTS.SOURCE.eq(sourceUser)
                        .and(FRIEND_REQUESTS.TARGET.eq(targetUser)))
                .fetchOptional()
                .isPresent();
    }

    @Override
    public List<UUID> requestsTo(UUID targetUser) {
        return context.select(FRIEND_REQUESTS.SOURCE)
                .where(FRIEND_REQUESTS.TARGET.eq(targetUser))
                .fetch(FRIEND_REQUESTS.SOURCE);
    }

    @Override
    public List<UUID> requestsFrom(UUID sourceUser) {
        return context.select(FRIEND_REQUESTS.TARGET)
                .where(FRIEND_REQUESTS.SOURCE.eq(sourceUser))
                .fetch(FRIEND_REQUESTS.TARGET);
    }

    private FriendRequestsRecord toRecord(UUID sourceUser, UUID targetUser) {
        return new FriendRequestsRecord(sourceUser, targetUser);
    }
}
