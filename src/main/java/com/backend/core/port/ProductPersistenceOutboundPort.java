package com.backend.core.port;

import com.backend.core.domain.Product;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterProductCommand;

import java.util.Optional;
import java.util.UUID;

public interface ProductPersistenceOutboundPort {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    PageContent<Product> findAll(FilterProductCommand filterProductCommand, Integer pageSize, Integer pageNumber);

    Product update(Product product);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean isProductUsedInOrders(UUID productId);
}
