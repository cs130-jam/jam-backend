package com.ucla.jam.util.pagination;

/**
 * Context for pagination from HTTP endpoints
 */
public interface PaginationContext {
    /**
     * Get the total number of pages for this pagination
     * @return non-negative number of total pages
     */
    int getTotalPages();
}
