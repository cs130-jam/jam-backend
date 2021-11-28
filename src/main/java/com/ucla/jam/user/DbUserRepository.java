package com.ucla.jam.user;

import generated.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;

import static generated.jooq.tables.Users.USERS;

@RequiredArgsConstructor
public class DbUserRepository implements UserRepository {

    private final DSLContext context;

    @Override
    public Optional<User> find(UUID userId) {
        return context.selectFrom(USERS)
                .where(USERS.ID.eq(userId))
                .fetchOptional(this::fromRecord);
    }

    @Override
    public void insert(User user) {
        context.insertInto(USERS)
                .set(toRecord(user))
                .onDuplicateKeyUpdate()
                .set(toRecord(user))
                .execute();
    }

    private User fromRecord(UsersRecord user) {
        return new User(user.getId(), user.getProfile(), user.getPreferences());
    }

    private UsersRecord toRecord(User user) {
        return new UsersRecord(user.getId(), user.getProfile(), user.getPreferences());
    }
}
