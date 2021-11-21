package com.ucla.jam.recommendation;

import java.util.List;
import java.util.UUID;

public interface VisitedRecommendationsRepository {
    List<UUID> getVisited(UUID userId);
    void markVisited(UUID userId, UUID targetId);
}
