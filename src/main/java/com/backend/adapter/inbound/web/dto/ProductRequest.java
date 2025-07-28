package com.backend.adapter.inbound.web.dto;

import com.backend.core.domain.enums.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank
        @Schema(description = "Product name", example = "Notebook")
        String name,

        @NotNull
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
        @Schema(description = "Product price", example = "150.00")
        BigDecimal price,

        @NotNull
        @Schema(description = "Product Type", example = "SERVICE")
        ProductType type,

        @Schema(description = "Order status", example = "true")
        Boolean active
) {
}
