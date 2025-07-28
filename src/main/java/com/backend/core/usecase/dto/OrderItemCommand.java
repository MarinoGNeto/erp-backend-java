package com.backend.core.usecase.dto;

import java.util.UUID;

public record OrderItemCommand(
        UUID productId,
        Integer quantity
) {
}
