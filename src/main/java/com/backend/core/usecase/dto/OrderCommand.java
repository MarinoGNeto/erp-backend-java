package com.backend.core.usecase.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCommand(
        BigDecimal discountPercentage,
        List<OrderItemCommand> items
) {
}
