package com.backend.adapter.inbound.web.dto;

import com.backend.core.domain.enums.OrderStatus;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

import java.math.BigDecimal;

@ParameterObject
public record FilterOrderRequest(

        @Parameter(description = "Order status")
        OrderStatus status,

        @Parameter(description = "Order discountPercentage")
        BigDecimal discountPercentage
) {
}
