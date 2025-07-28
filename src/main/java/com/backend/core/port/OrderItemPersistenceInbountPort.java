package com.backend.core.port;

import com.backend.core.domain.OrderItem;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderItemCommand;
import com.backend.core.usecase.dto.OrderItemCommand;

import java.util.UUID;

public interface OrderItemPersistenceInbountPort {

    OrderItem create(UUID orderId, OrderItemCommand orderItemCommand);

    OrderItem findByIdAndOrderId(UUID orderId, UUID orderItemId);

    PageContent<OrderItem> findAll(UUID orderId, FilterOrderItemCommand filterOrderItemCommand, Integer pageSize, Integer pageNumber);

    OrderItem update(OrderItemCommand orderItemCommand, UUID orderId, UUID orderItemId);

    void deleteById(UUID orderId,  UUID orderItemId);




}
