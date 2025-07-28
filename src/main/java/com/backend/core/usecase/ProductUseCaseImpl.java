package com.backend.core.usecase;

import com.backend.core.domain.Product;
import com.backend.core.port.ProductInboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.FilterProductCommand;
import com.backend.core.usecase.dto.ProductCommand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class ProductUseCaseImpl implements ProductInboundPort {

    private static final String PRODUCT_NOT_FOUND_WITH_ID_MESSAGE = "Product not found with id: [%s]";
    private static final String PRODUCT_IN_USE_MESSAGE = "Cannot delete product because it is used in orders.";

    private final ProductOutboundPort productOutboundPort;

    public ProductUseCaseImpl(ProductOutboundPort productOutboundPort) {
        this.productOutboundPort = productOutboundPort;
    }

    @Override
    @Transactional
    public Product create(ProductCommand productCommand) {
        Product product = new Product();
        product.setName(productCommand.name());
        product.setPrice(productCommand.price());
        product.setType(productCommand.type());
        product.setActive(productCommand.active());

        return productOutboundPort.save(product);
    }

    @Override
    public Product findById(UUID id) {
        return findProductByIdOrThrow(id);
    }

    @Override
    public PageContent<Product> findAll(FilterProductCommand filterProductCommand, Integer pageSize, Integer pageNumber) {
        return productOutboundPort.findAll(filterProductCommand, pageSize, pageNumber);
    }

    @Override
    @Transactional
    public Product update(ProductCommand productCommand, UUID id) {
        Product product = findProductByIdOrThrow(id);

        product.setName(productCommand.name());
        product.setPrice(productCommand.price());
        product.setType(productCommand.type());
        product.setActive(productCommand.active());

        return productOutboundPort.update(product);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!productOutboundPort.existsById(id)) {
            throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MESSAGE, id));
        }

        if (productOutboundPort.isProductUsedInOrders(id)) {
            throw new IllegalArgumentException(PRODUCT_IN_USE_MESSAGE);
        }

        productOutboundPort.deleteById(id);
    }

    private Product findProductByIdOrThrow(UUID id) {
        return productOutboundPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MESSAGE, id)));
    }
}
