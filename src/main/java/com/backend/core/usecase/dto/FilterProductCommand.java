package com.backend.core.usecase.dto;

import com.backend.core.domain.enums.ProductType;

public record FilterProductCommand(
        String name,
        ProductType type,
        Boolean active
) {
}
