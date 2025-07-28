package com.backend.core.usecase.dto;

import com.backend.core.domain.enums.OrderStatus;

import java.math.BigDecimal;

public record FilterOrderCommand(
        OrderStatus status,
        BigDecimal discountPercentage
) {
}
