package com.backend.adapter.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        @DecimalMin("0.0")
        @DecimalMax("100.0")
        @Schema(description = "Order discount percentage", example = "20.00")
        BigDecimal discountPercentage,

        @NotNull
        @Schema(description = "Order items", example = "[]")
        List<@Valid OrderItemRequest> items
) {
}
