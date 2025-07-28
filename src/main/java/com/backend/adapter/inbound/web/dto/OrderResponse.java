package com.backend.adapter.inbound.web.dto;

import com.backend.core.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        @Schema(description = "Order ID", example = "c8e64fad-dc85-4dad-b599-a33a4b4d7db2")
        UUID id,
        @Schema(description = "Order Status", example = "OPEN")
        OrderStatus status,
        @Schema(description = "Order discount percentage", example = "20.00")
        BigDecimal discountPercentage,
        @Schema(description = "Order total value", example = "100.00")
        BigDecimal total,
        @Schema(description = "Order items", example = "[]")
        List<OrderItemResponse> items,
        @Schema(description = "Order creation date", example = "2025-07-27 19:33:55.583")
        LocalDateTime createdAt

) {
}
