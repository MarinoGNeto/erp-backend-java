package com.backend.adapter.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequest(
        @NotNull
        @Schema(description = "Product ID", example = "c8e64fad-dc85-4dad-b599-a33a4b4d7db2")
        UUID productId,

        @NotNull
        @Min(1)
        @Schema(description = "Quantity of items", example = "10")
        Integer quantity
) {
}
