package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.OrderItemResponse;
import com.backend.core.domain.OrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemToOrderItemResponseConverter implements Converter<OrderItem, OrderItemResponse> {

    @Override
    public OrderItemResponse convert(OrderItem source) {
        return new OrderItemResponse(
                source.getId(),
                source.getProduct().getId(),
                source.getProduct().getName(),
                source.getUnitPrice(),
                source.getQuantity()
        );
    }
}
