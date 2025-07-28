package com.backend.core.port;

import com.backend.core.domain.Product;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterProductCommand;
import com.backend.core.usecase.dto.ProductCommand;

import java.util.UUID;

public interface ProductPersistenceInboundPort {

    Product create(ProductCommand productCommand);

    Product findById(UUID id);

    PageContent<Product> findAll(FilterProductCommand filterProductCommand, Integer pageSize, Integer pageNumber);

    Product update(ProductCommand productCommand, UUID id);

    void deleteById(UUID id);
}
