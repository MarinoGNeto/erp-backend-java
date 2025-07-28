package com.backend.core.usecase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FilterOrderItemCommand(

        UUID productId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
