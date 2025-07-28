package com.backend.adapter.inbound.web.dto;

import com.backend.core.domain.enums.ProductType;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public record FilterProductRequest(

        @Parameter(description = "Product Name")
        String name,
        @Parameter(description = "Product type")
        ProductType type,
        @Parameter(description = "Product status")
        Boolean active
) {
}
