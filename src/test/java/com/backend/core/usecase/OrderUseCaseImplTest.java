package com.backend.core.usecase;

import com.backend.core.domain.Order;
import com.backend.core.domain.Product;
import com.backend.core.domain.enums.OrderStatus;
import com.backend.core.domain.enums.ProductType;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.OrderCommand;
import com.backend.core.usecase.dto.OrderItemCommand;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderUseCaseImplTest {

    private OrderOutboundPort orderOutboundPort;
    private ProductOutboundPort productOutboundPort;
    private OrderUseCaseImpl orderUseCase;

    @BeforeEach
    void setUp() {
        orderOutboundPort = mock(OrderOutboundPort.class);
        productOutboundPort = mock(ProductOutboundPort.class);
        orderUseCase = new OrderUseCaseImpl(orderOutboundPort, productOutboundPort);
    }

    @Test
    @DisplayName("Should create order with discount applied to products only")
    void shouldBeAbleToCreateOrderWithProductDiscount() {
        // Assemble
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Test Product", BigDecimal.TEN, ProductType.PRODUCT, true);
        OrderItemCommand itemCommand = new OrderItemCommand(productId, 2);
        OrderCommand orderCommand = new OrderCommand(BigDecimal.valueOf(10), Collections.singletonList(itemCommand));

        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));
        when(orderOutboundPort.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Actions
        Order created = orderUseCase.create(orderCommand);

        // Assertions
        assertNotNull(created);
        assertEquals(1, created.getItems().size());
        assertEquals(BigDecimal.valueOf(18.00).setScale(2), created.getTotal().setScale(2));
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        // Assemble
        UUID productId = UUID.randomUUID();
        OrderItemCommand itemCommand = new OrderItemCommand(productId, 1);
        OrderCommand command = new OrderCommand(BigDecimal.ZERO, Collections.singletonList(itemCommand));

        when(productOutboundPort.findById(productId)).thenReturn(Optional.empty());

        // Actions & Assertions
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> orderUseCase.create(command));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    @DisplayName("Should throw exception when product is inactive")
    void shouldThrowExceptionWhenProductIsInactive() {
        // Assemble
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Inactive", BigDecimal.TEN, ProductType.PRODUCT, false);
        OrderItemCommand itemCommand = new OrderItemCommand(productId, 1);
        OrderCommand command = new OrderCommand(BigDecimal.ZERO, Collections.singletonList(itemCommand));

        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));

        // Actions & Assertions
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> orderUseCase.create(command));
        assertTrue(ex.getMessage().contains("inactive product"));
    }

    @Test
    @DisplayName("Should update order and recalculate total")
    void shouldBeAbleToUpdateOrderSuccessfully() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Product", BigDecimal.TEN, ProductType.PRODUCT, true);

        OrderItemCommand itemCommand = new OrderItemCommand(productId, 3);
        OrderCommand command = new OrderCommand(BigDecimal.valueOf(10), Collections.singletonList(itemCommand));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.OPEN);

        when(orderOutboundPort.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));
        when(orderOutboundPort.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Actions
        Order updated = orderUseCase.update(command, orderId);

        // Assertions
        assertEquals(BigDecimal.valueOf(27).setScale(2), updated.getTotal().setScale(2));
        assertEquals(1, updated.getItems().size());
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldBeAbleToDeleteOrder() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        when(orderOutboundPort.existsById(orderId)).thenReturn(true);
        doNothing().when(orderOutboundPort).deleteById(orderId);

        // Actions
        orderUseCase.deleteById(orderId);

        // Assertions
        verify(orderOutboundPort, times(1)).deleteById(orderId);
    }

    @Test
    @DisplayName("Should throw exception when trying to delete non-existing order")
    void shouldThrowExceptionWhenDeletingNonExistentOrder() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        when(orderOutboundPort.existsById(orderId)).thenReturn(false);

        // Actions & Assertions
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> orderUseCase.deleteById(orderId));
        assertTrue(ex.getMessage().contains("Order not found"));
    }
}
