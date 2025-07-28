package com.backend.core.usecase;

import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import com.backend.core.domain.Product;
import com.backend.core.domain.enums.ProductType;
import com.backend.core.port.OrderItemOutboundPort;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.OrderItemCommand;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderItemUseCaseImplTest {

    private OrderItemUseCaseImpl useCase;
    private OrderItemOutboundPort orderItemOutboundPort;
    private ProductOutboundPort productOutboundPort;
    private OrderOutboundPort orderOutboundPort;

    @BeforeEach
    void setup() {
        orderItemOutboundPort = mock(OrderItemOutboundPort.class);
        productOutboundPort = mock(ProductOutboundPort.class);
        orderOutboundPort = mock(OrderOutboundPort.class);

        useCase = new OrderItemUseCaseImpl(orderItemOutboundPort, productOutboundPort, orderOutboundPort);
    }

    @Test
    @DisplayName("Should be able to create order item when product is active")
    void shouldBeAbleToCreateOrderItemWhenProductIsActive() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Test", BigDecimal.TEN, ProductType.PRODUCT, true);
        Order order = new Order();

        OrderItemCommand command = new OrderItemCommand(productId, 2);

        when(orderOutboundPort.findById(orderId)).thenReturn(Optional.of(order));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));

        OrderItem expectedItem = new OrderItem();
        expectedItem.setProduct(product);
        expectedItem.setQuantity(2);
        expectedItem.setUnitPrice(BigDecimal.TEN);

        when(orderItemOutboundPort.save(eq(order), any(OrderItem.class))).thenReturn(expectedItem);

        // Actions
        OrderItem result = useCase.create(orderId, command);

        // Assertions
        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Should throw exception when product is inactive")
    void shouldThrowExceptionWhenProductIsInactive() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Inactive", BigDecimal.TEN, ProductType.PRODUCT, false);
        Order order = new Order();

        OrderItemCommand command = new OrderItemCommand(productId, 1);

        when(orderOutboundPort.findById(orderId)).thenReturn(Optional.of(order));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));

        // Actions & Assertions
        assertThatThrownBy(() -> useCase.create(orderId, command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot use inactive product");
    }

    @Test
    @DisplayName("Should throw exception when order item does not exist on delete")
    void shouldThrowExceptionWhenOrderItemDoesNotExistOnDelete() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID orderItemId = UUID.randomUUID();

        Order order = new Order();
        order.setItems(java.util.Collections.emptyList());

        when(orderOutboundPort.findById(orderId)).thenReturn(Optional.of(order));

        // Actions + Assertions
        assertThatThrownBy(() -> useCase.deleteById(orderId, orderItemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order Item not found");
    }

    @Test
    @DisplayName("Should update order item successfully")
    void shouldUpdateOrderItemSuccessfully() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderItemCommand command = new OrderItemCommand(productId, 2);

        Product product = new Product();
        product.setId(productId);
        product.setActive(true);
        product.setPrice(new BigDecimal("15.00"));
        product.setType(ProductType.PRODUCT);

        OrderItem existingItem = new OrderItem();
        existingItem.setId(itemId);
        existingItem.setQuantity(1);
        existingItem.setProduct(product);
        existingItem.setUnitPrice(BigDecimal.TEN);

        OrderItem updatedItem = new OrderItem();
        updatedItem.setId(itemId);
        updatedItem.setQuantity(2);
        updatedItem.setProduct(product);
        updatedItem.setUnitPrice(product.getPrice());

        when(orderItemOutboundPort.findByIdAndOrderId(orderId, itemId)).thenReturn(Optional.of(existingItem));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemOutboundPort.update(orderId, itemId, existingItem)).thenReturn(updatedItem);

        // Actions
        OrderItem result = useCase.update(command, orderId, itemId);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(itemId);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualByComparingTo("15.00");

        verify(orderItemOutboundPort).update(orderId, itemId, existingItem);
    }

    @Test
    @DisplayName("Should throw exception if order item not found on update")
    void shouldThrowIfOrderItemNotFoundOnUpdate() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        OrderItemCommand command = new OrderItemCommand(UUID.randomUUID(), 1);

        when(orderItemOutboundPort.findByIdAndOrderId(orderId, itemId)).thenReturn(Optional.empty());

        // Actions + Assertions
        assertThatThrownBy(() -> useCase.update(command, orderId, itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order Item not found with id");
    }

    @Test
    @DisplayName("Should throw exception if product is inactive on update")
    void shouldThrowIfProductIsInactiveOnUpdate() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemCommand command = new OrderItemCommand(productId, 1);

        Product inactiveProduct = new Product();
        inactiveProduct.setId(productId);
        inactiveProduct.setActive(false);

        OrderItem existingItem = new OrderItem();
        existingItem.setId(itemId);

        when(orderItemOutboundPort.findByIdAndOrderId(orderId, itemId)).thenReturn(Optional.of(existingItem));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.of(inactiveProduct));

        // Actions + Assertions
        assertThatThrownBy(() -> useCase.update(command, orderId, itemId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot use inactive product");
    }

    @Test
    @DisplayName("Should throw exception if product not found on update")
    void shouldThrowIfProductNotFoundOnUpdate() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderItemCommand command = new OrderItemCommand(productId, 1);

        OrderItem existingItem = new OrderItem();
        existingItem.setId(itemId);

        when(orderItemOutboundPort.findByIdAndOrderId(orderId, itemId)).thenReturn(Optional.of(existingItem));
        when(productOutboundPort.findById(productId)).thenReturn(Optional.empty());

        // Actions + Assertions
        assertThatThrownBy(() -> useCase.update(command, orderId, itemId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found with id");
    }

    @Test
    @DisplayName("Should be able to delete order item")
    void shouldBeAbleToDeleteOrderItem() {
        // Assemble
        UUID orderId = UUID.randomUUID();
        UUID orderItemId = UUID.randomUUID();

        OrderItem item = new OrderItem();
        item.setId(orderItemId);

        Order order = new Order();
        order.setItems(new java.util.ArrayList<>(java.util.List.of(item)));

        when(orderOutboundPort.findById(orderId)).thenReturn(Optional.of(order));

        // Actions
        useCase.deleteById(orderId, orderItemId);

        // Assertions
        assertThat(order.getItems()).isEmpty();
        verify(orderOutboundPort).update(order);
    }
}
