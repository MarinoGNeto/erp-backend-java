package com.backend.adapter.inbound.web.dto;

import com.backend.core.domain.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        @Schema(description = "Product ID", example = "455606bc-cde6-4ef7-a2c4-6c4579a89132")
        UUID id,
        @Schema(description = "Product name", example = "Notebook")
        String name,
        @Schema(description = "Product price", example = "150.00")
        BigDecimal price,
        @Schema(description = "Product type", example = "PRODUCT")
        ProductType type,
        @Schema(description = "Product status", example = "true")
        boolean active
) {
}
