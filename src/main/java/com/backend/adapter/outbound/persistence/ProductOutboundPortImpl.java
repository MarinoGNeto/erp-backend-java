package com.backend.adapter.outbound.persistence;

import com.backend.adapter.outbound.persistence.entity.ProductEntity;
import com.backend.adapter.outbound.persistence.entity.QProductEntity;
import com.backend.adapter.outbound.persistence.repository.ProductRepository;
import com.backend.core.domain.Product;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterProductCommand;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ProductOutboundPortImpl implements ProductOutboundPort {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    public ProductOutboundPortImpl(ConversionService conversionService, ProductRepository productRepository) {
        this.conversionService = conversionService;
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = Objects.requireNonNull(conversionService.convert(product, ProductEntity.class), "Converted Product entity cannot be null");
        ProductEntity saved = productRepository.save(productEntity);

        return conversionService.convert(saved, Product.class);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);

        return optionalProduct.map(productEntity -> Objects.requireNonNull(conversionService.convert(productEntity, Product.class),
                "Converted Product cannot be null"));

    }

    @Override
    public PageContent<Product> findAll(FilterProductCommand filterProductCommand, Integer pageSize, Integer pageNumber) {
        QProductEntity qProduct = QProductEntity.productEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.isNotBlank(filterProductCommand.name())) {
            builder.and(qProduct.name.containsIgnoreCase(filterProductCommand.name()));
        }

        if (Objects.nonNull(filterProductCommand.type())) {
            builder.and(qProduct.type.eq(filterProductCommand.type()));
        }

        if (Objects.nonNull(filterProductCommand.active())) {
            builder.and(qProduct.active.eq(filterProductCommand.active()));
        }

        Page<ProductEntity> pages = productRepository.findAll(builder, PageRequest.of(pageNumber, pageSize));
        List<Product> productList = pages.getContent()
                .stream()
                .map(productEntity -> conversionService.convert(productEntity, Product.class))
                .toList();

        return new PageContent<>(productList, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber());
    }

    @Override
    public Product update(Product product) {
        ProductEntity productEntity = Objects.requireNonNull(conversionService.convert(product, ProductEntity.class), "Converted Product entity cannot be null");
        ProductEntity savedProduct = productRepository.save(productEntity);

        return conversionService.convert(savedProduct, Product.class);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean isProductUsedInOrders(UUID productId) {
        return productRepository.isProductUsedInOrders(productId);
    }
}
