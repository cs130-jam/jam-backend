package com.ucla.jam.util.pagination;

import java.util.List;

public interface PaginatedResponse<T> {
    PaginationContext getPagination();
    List<T> getItems();
}
