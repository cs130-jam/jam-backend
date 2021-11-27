package com.ucla.jam.util.pagination;

import java.util.List;

/**
 * Response from an http endpoint which uses pagination
 * @param <T> Type of records actually returned
 */
public interface PaginatedResponse<T> {

    /**
     * Get information about the current pagination
     * @return Pagination context
     */
    PaginationContext getPagination();

    /**
     * Get items in the current page
     * @return List of items
     */
    List<T> getItems();
}
