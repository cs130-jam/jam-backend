package com.ucla.jam.recommendation;

import generated.jooq.tables.records.VisitedRecsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static generated.jooq.Tables.VISITED_RECS;

@RequiredArgsConstructor
public class DbVisitedRecommendationsRepository implements VisitedRecommendationsRepository {

    private final DSLContext context;

    @Override
    public List<UUID> getVisited(UUID userId) {
        return context.selectFrom(VISITED_RECS)
                .where(VISITED_RECS.USERID.eq(userId))
                .fetch(record -> record.get(VISITED_RECS.TARGETID));
    }

    @Override
    public void markVisited(UUID userId, UUID targetId) {
        context.insertInto(VISITED_RECS)
                .set(new VisitedRecsRecord(userId, targetId))
                .onDuplicateKeyIgnore();
    }
}
