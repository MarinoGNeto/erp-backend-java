package com.backend.adapter.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        @Schema(description = "Order ID", example = "c8e64fad-dc85-4dad-b599-a33a4b4d7db2")
        UUID id,
        @Schema(description = "Product ID", example = "455606bc-cde6-4ef7-a2c4-6c4579a89132")
        UUID productId,
        @Schema(description = "Product name", example = "Notebook")
        String productName,
        @Schema(description = "Unit price of item", example = "10.00")
        BigDecimal unitPrice,
        @Schema(description = "Quantity of items", example = "15")
        Integer quantity
) {
}
