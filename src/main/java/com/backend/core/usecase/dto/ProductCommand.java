package com.backend.core.usecase.dto;

import com.backend.core.domain.enums.ProductType;

import java.math.BigDecimal;

public record ProductCommand(
        String name,
        BigDecimal price,
        ProductType type,
        boolean active
) {
}
