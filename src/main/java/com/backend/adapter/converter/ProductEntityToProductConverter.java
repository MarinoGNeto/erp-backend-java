package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.ProductEntity;
import com.backend.core.domain.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityToProductConverter implements Converter<ProductEntity, Product> {

    @Override
    public Product convert(ProductEntity source) {
        Product product = new Product();
        product.setId(source.getId());
        product.setName(source.getName());
        product.setPrice(source.getPrice());
        product.setType(source.getType());
        product.setActive(source.isActive());
        return product;
    }
}
