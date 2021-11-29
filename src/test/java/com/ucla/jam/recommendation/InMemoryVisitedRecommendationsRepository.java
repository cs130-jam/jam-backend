package com.ucla.jam.recommendation;

import com.google.common.collect.ImmutableSet;

import java.util.*;

public class InMemoryVisitedRecommendationsRepository implements VisitedRecommendationsRepository {

    private final Map<UUID, Set<UUID>> data = new HashMap<>();

    @Override
    public List<UUID> getVisited(UUID userId) {
        return List.copyOf(data.getOrDefault(userId, Set.of()));
    }

    @Override
    public void markVisited(UUID userId, UUID targetId) {
        data.put(userId, ImmutableSet.<UUID>builder()
                .addAll(getVisited(userId))
                .add(targetId)
                .build());
    }
}
