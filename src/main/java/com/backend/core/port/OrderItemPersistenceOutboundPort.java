package com.backend.core.port;

import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderItemCommand;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemPersistenceOutboundPort {
    OrderItem save(Order order, OrderItem item);

    Optional<OrderItem> findByIdAndOrderId(UUID orderId, UUID orderItemId);

    PageContent<OrderItem> findAll(UUID orderId, FilterOrderItemCommand filterOrderItemCommand, Integer pageSize, Integer pageNumber);

    OrderItem update(UUID orderId, UUID orderItemId, OrderItem orderItem);
}
