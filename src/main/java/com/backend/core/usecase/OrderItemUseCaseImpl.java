package com.backend.core.usecase;

import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import com.backend.core.domain.Product;
import com.backend.core.port.OrderItemInboundPort;
import com.backend.core.port.OrderItemOutboundPort;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.FilterOrderItemCommand;
import com.backend.core.usecase.dto.OrderItemCommand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderItemUseCaseImpl implements OrderItemInboundPort {

    private static final String ORDER_NOT_FOUND = "Order not found with id: [%s]";
    private static final String PRODUCT_NOT_FOUND = "Product not found with id: [%s]";
    private static final String PRODUCT_INACTIVE = "Cannot use inactive product with id: [%s]";
    private static final String ORDER_ITEM_NOT_FOUND = "Order Item not found with id: [%s]";
    private static final String ORDER_ITEM_NOT_FOUND_WITH_BOTH_IDS = "Order Item not found with id: [%s] and order Id: [%s]";

    private final OrderItemOutboundPort orderItemOutboundPort;
    private final ProductOutboundPort productOutboundPort;
    private final OrderOutboundPort orderOutboundPort;

    public OrderItemUseCaseImpl(OrderItemOutboundPort orderItemOutboundPort,
                                ProductOutboundPort productOutboundPort,
                                OrderOutboundPort orderOutboundPort) {
        this.orderItemOutboundPort = orderItemOutboundPort;
        this.productOutboundPort = productOutboundPort;
        this.orderOutboundPort = orderOutboundPort;
    }

    @Override
    @Transactional
    public OrderItem create(UUID orderId, OrderItemCommand command) {
        Order order = findOrderById(orderId);
        Product product = findActiveProductById(command.productId());

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(command.quantity());
        item.setUnitPrice(product.getPrice());

        return orderItemOutboundPort.save(order, item);
    }

    @Override
    public OrderItem findByIdAndOrderId(UUID orderId, UUID orderItemId) {
        return findOrderItemByIdAndOrderId(orderId, orderItemId);
    }

    @Override
    public PageContent<OrderItem> findAll(UUID orderId, FilterOrderItemCommand filter, Integer pageSize, Integer pageNumber) {
        return orderItemOutboundPort.findAll(orderId, filter, pageSize, pageNumber);
    }

    @Override
    @Transactional
    public OrderItem update(OrderItemCommand command, UUID orderId, UUID orderItemId) {
        OrderItem item = findOrderItemByIdAndOrderId(orderId, orderItemId);
        Order order = findOrderById(orderId);
        Product product = findActiveProductById(command.productId());

        item.setProduct(product);
        item.setOrder(order);
        item.setQuantity(command.quantity());
        item.setUnitPrice(product.getPrice());

        return orderItemOutboundPort.update(item);
    }

    @Override
    @Transactional
    public void deleteById(UUID orderId, UUID orderItemId) {
        Order order = findOrderById(orderId);

        List<OrderItem> mutableItems = new ArrayList<>(order.getItems());
        boolean removed = mutableItems.removeIf(item -> item.getId().equals(orderItemId));
        if (!removed) {
            throw new EntityNotFoundException(String.format(ORDER_ITEM_NOT_FOUND, orderItemId));
        }

        order.setItems(mutableItems);
        orderOutboundPort.update(order);
    }

    private Order findOrderById(UUID orderId) {
        return orderOutboundPort.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    private Product findActiveProductById(UUID productId) {
        Product product = productOutboundPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));

        if (!product.isActive()) {
            throw new IllegalArgumentException(String.format(PRODUCT_INACTIVE, productId));
        }

        return product;
    }

    private OrderItem findOrderItemByIdAndOrderId(UUID orderId, UUID orderItemId) {
        return orderItemOutboundPort.findByIdAndOrderId(orderId, orderItemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ORDER_ITEM_NOT_FOUND_WITH_BOTH_IDS, orderItemId, orderId)));
    }
}
