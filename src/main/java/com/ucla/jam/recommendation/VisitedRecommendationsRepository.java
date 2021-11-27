package com.ucla.jam.recommendation;

import java.util.List;
import java.util.UUID;

public interface VisitedRecommendationsRepository {

    /**
     * Get all recommendations which the given user has already seen.
     * @param userId User UUID
     * @return List of UUIDs of users that given user has already seen
     */
    List<UUID> getVisited(UUID userId);

    /**
     * Mark a recommendation as viewed for given user.
     * @param userId User UUID
     * @param targetId Recommended user UUID
     */
    void markVisited(UUID userId, UUID targetId);
}
