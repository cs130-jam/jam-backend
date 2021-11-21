package com.ucla.jam.recommendation.responses;

import com.ucla.jam.util.pagination.PaginatedResponse;
import com.ucla.jam.util.pagination.PaginationContext;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class GetRecommendationsResponse implements PaginationContext, PaginatedResponse<UUID> {

    int totalPages;
    List<UUID> users;

    @Override
    public PaginationContext getPagination() {
        return this;
    }

    @Override
    public List<UUID> getItems() {
        return users;
    }
}
