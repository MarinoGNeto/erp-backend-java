package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.ProductEntity;
import com.backend.core.domain.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductEntityConverter implements Converter<Product, ProductEntity> {

    @Override
    public ProductEntity convert(Product source) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(source.getId());
        productEntity.setName(source.getName());
        productEntity.setPrice(source.getPrice());
        productEntity.setType(source.getType());
        productEntity.setActive(source.isActive());
        return productEntity;
    }
}
