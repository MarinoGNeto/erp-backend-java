package com.backend.adapter.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record Pagination<T>(
        @Schema(description = "Items list", example = "[]")
        List<T> content,
        @Schema(description = "Total pages", example = "10")
        int totalPages,
        @Schema(description = "Total elements", example = "100")
        long totalElements,
        @Schema(description = "Actual Page", example = "1")
        int pageNumber
) {
}
