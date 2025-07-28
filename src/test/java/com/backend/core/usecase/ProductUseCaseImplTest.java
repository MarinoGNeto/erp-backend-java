package com.backend.core.usecase;

import com.backend.core.domain.Product;
import com.backend.core.domain.enums.ProductType;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.ProductCommand;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductUseCaseImplTest {

    private ProductOutboundPort productOutboundPort;
    private ProductUseCaseImpl productUseCaseImpl;

    @BeforeEach
    void setUp() {
        productOutboundPort = mock(ProductOutboundPort.class);
        productUseCaseImpl = new ProductUseCaseImpl(productOutboundPort);
    }

    @Test
    @DisplayName("Should be able to create product successfully")
    void shouldBeAbleToCreateProductSuccessfully() {
        //Assemble
        ProductCommand command = new ProductCommand("Notebook", new BigDecimal("3500.00"), ProductType.PRODUCT, true);
        Product product = new Product();

        when(productOutboundPort.save(any())).thenReturn(product);

        //Actions
        Product result = productUseCaseImpl.create(command);

        // Assertions
        assertNotNull(result);
        verify(productOutboundPort).save(any());
    }

    @Test
    @DisplayName("Should be able to create service successfully")
    void shouldBeAbleToCreateServiceSuccessfully() {
        //Assemble
        ProductCommand command = new ProductCommand("Manutencao", new BigDecimal("200.00"), ProductType.SERVICE, true);
        Product product = new Product();

        when(productOutboundPort.save(any())).thenReturn(product);

        //Actions
        Product result = productUseCaseImpl.create(command);

        // Assertions
        assertNotNull(result);
        verify(productOutboundPort).save(any());
    }

    @Test
    @DisplayName("Should throw exception when finding non existent product")
    void shouldThrowExceptionWhenFindingNonExistentProduct() {
        //Assemble
        UUID id = UUID.randomUUID();

        when(productOutboundPort.findById(id)).thenReturn(Optional.empty());

        // Assertions
        assertThrows(EntityNotFoundException.class, () -> productUseCaseImpl.findById(id));
    }

    @Test
    @DisplayName("Should be able to update product")
    void shouldBeAbleToUpdateProduct() {
        //Assemble
        UUID id = UUID.randomUUID();
        Product existing = new Product();
        ProductCommand command = new ProductCommand("Atualizado", new BigDecimal("120.00"), ProductType.PRODUCT, false);

        when(productOutboundPort.findById(id)).thenReturn(Optional.of(existing));
        when(productOutboundPort.update(any())).thenReturn(existing);

        // Actions
        Product result = productUseCaseImpl.update(command, id);

        // Assertions
        assertNotNull(result);
        verify(productOutboundPort).update(existing);
    }

    @Test
    @DisplayName("Should throw exception when updating non existent product")
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        // Assemble
        UUID id = UUID.randomUUID();
        ProductCommand command = new ProductCommand("X", BigDecimal.ONE, ProductType.PRODUCT, true);

        when(productOutboundPort.findById(id)).thenReturn(Optional.empty());

        // Actions & Assertions
        assertThrows(EntityNotFoundException.class, () -> productUseCaseImpl.update(command, id));
    }

    @Test
    @DisplayName("Should be able to delete product")
    void shouldBeAbleToDeleteProduct() {
        // Assemble
        UUID id = UUID.randomUUID();

        when(productOutboundPort.existsById(id)).thenReturn(true);
        when(productOutboundPort.isProductUsedInOrders(id)).thenReturn(false);

        // Actions
        productUseCaseImpl.deleteById(id);

        // Assertions
        verify(productOutboundPort).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existent product")
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // Assemble
        UUID id = UUID.randomUUID();

        when(productOutboundPort.existsById(id)).thenReturn(false);

        // Actions & Assertions
        assertThrows(EntityNotFoundException.class, () -> productUseCaseImpl.deleteById(id));
    }

    @Test
    @DisplayName("Should throw exception when deleting product in use")
    void shouldThrowExceptionWhenDeletingProductInUse() {
        // Assemble
        UUID id = UUID.randomUUID();

        when(productOutboundPort.existsById(id)).thenReturn(true);
        when(productOutboundPort.isProductUsedInOrders(id)).thenReturn(true);

        // Actions & Assertions
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> productUseCaseImpl.deleteById(id));
        assertEquals("Cannot delete product because it is used in orders.", ex.getMessage());
    }
}
