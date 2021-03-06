package com.ucla.jam.user.authentication;

import generated.jooq.tables.records.InternalCredentialsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Optional;
import java.util.UUID;

import static generated.jooq.Tables.INTERNAL_CREDENTIALS;

@RequiredArgsConstructor
public class DbInternalCredentialsRepository implements CredentialsRepository {

    private final DSLContext context;

    @Override
    public Optional<UUID> userForCredentials(Credentials genericCredentials) {
        InternalCredentials credentials = (InternalCredentials) genericCredentials;
        return context.select(INTERNAL_CREDENTIALS.USERID)
                .from(INTERNAL_CREDENTIALS)
                .where(INTERNAL_CREDENTIALS.USERNAME.eq(credentials.getUsername())
                        .and(INTERNAL_CREDENTIALS.PASSWORD_HASH.eq(credentials.getPasswordHash())))
                .fetchOptional(INTERNAL_CREDENTIALS.USERID);
    }

    @Override
    public void addUser(UUID id, Credentials genericCredentials) {
        InternalCredentials credentials = (InternalCredentials) genericCredentials;
        context.insertInto(INTERNAL_CREDENTIALS)
                .set(toRecord(id, credentials))
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public boolean isUserExist(Credentials partialCredentials) {
        InternalCredentials credentials = (InternalCredentials) partialCredentials;
        return context.selectFrom(INTERNAL_CREDENTIALS)
                .where(INTERNAL_CREDENTIALS.USERNAME.eq(credentials.getUsername()))
                .fetchOptional()
                .isPresent();
    }

    private InternalCredentialsRecord toRecord(UUID id, InternalCredentials credentials) {
        return new InternalCredentialsRecord(credentials.getUsername(), id, credentials.getPasswordHash());
    }
}
