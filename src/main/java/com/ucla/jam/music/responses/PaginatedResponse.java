package com.ucla.jam.music.responses;

import java.util.List;

public interface PaginatedResponse<T> {
    Pagination getPagination();
    List<T> getItems();
}
