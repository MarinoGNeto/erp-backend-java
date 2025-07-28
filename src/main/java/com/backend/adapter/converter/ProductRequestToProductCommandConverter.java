package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.ProductRequest;
import com.backend.core.usecase.dto.ProductCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestToProductCommandConverter implements Converter<ProductRequest, ProductCommand> {

    @Override
    public ProductCommand convert(ProductRequest source) {
        return new ProductCommand(
                source.name(),
                source.price(),
                source.type(),
                source.active()
        );
    }
}
