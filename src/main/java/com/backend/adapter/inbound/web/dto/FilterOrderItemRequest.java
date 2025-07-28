package com.backend.adapter.inbound.web.dto;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

import java.math.BigDecimal;
import java.util.UUID;

@ParameterObject
public record FilterOrderItemRequest(

        @Parameter(description = "Product ID")
        UUID productId,

        @Parameter(description = "Item quantity")
        Integer quantity,

        @Parameter(description = "Unit Price")
        BigDecimal unitPrice
) {
}
