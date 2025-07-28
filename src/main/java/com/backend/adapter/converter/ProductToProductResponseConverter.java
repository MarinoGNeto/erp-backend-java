package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.ProductResponse;
import com.backend.core.domain.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductResponseConverter implements Converter<Product, ProductResponse> {
    @Override
    public ProductResponse convert(Product source) {
        return new ProductResponse(
                source.getId(),
                source.getName(),
                source.getPrice(),
                source.getType(),
                source.isActive()
        );
    }
}
