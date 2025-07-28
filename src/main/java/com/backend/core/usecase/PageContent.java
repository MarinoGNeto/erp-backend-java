package com.backend.core.usecase;

import java.util.List;

public record PageContent<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int pageNumber
) {
}
