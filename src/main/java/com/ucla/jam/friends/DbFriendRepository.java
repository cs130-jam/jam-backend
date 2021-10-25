package com.ucla.jam.friends;

import static generated.jooq.Tables.FRIENDS;

import generated.jooq.tables.records.FriendsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DbFriendRepository implements FriendRepository {

    private final DSLContext context;

    @Override
    public List<UUID> getAll(UUID user) {
        return context.selectFrom(FRIENDS)
                .where(FRIENDS.USERA.eq(user)
                        .or(FRIENDS.USERB.eq(user)))
                .fetch(record -> record.getUsera().equals(user)
                        ? record.getUserb()
                        : record.getUsera());
    }

    @Override
    public void friend(UUID userA, UUID userB) {
        UUID lowerUser = userA.compareTo(userB) < 0 ? userA : userB;
        UUID upperUser = userA.compareTo(userB) > 0 ? userA : userB;
        context.insertInto(FRIENDS)
                .set(toRecord(lowerUser, upperUser))
                .execute();
    }

    @Override
    public void unfriend(UUID userA, UUID userB) {
        UUID lowerUser = userA.compareTo(userB) < 0 ? userA : userB;
        UUID upperUser = userA.compareTo(userB) > 0 ? userA : userB;
        context.deleteFrom(FRIENDS)
                .where(FRIENDS.USERA.eq(lowerUser)
                        .and(FRIENDS.USERB.eq(upperUser)))
                .execute();
    }

    private FriendsRecord toRecord(UUID userA, UUID userB) {
        return new FriendsRecord(userA, userB);
    }
}
