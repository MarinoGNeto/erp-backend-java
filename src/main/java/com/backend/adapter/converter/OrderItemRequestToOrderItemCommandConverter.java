package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.OrderItemRequest;
import com.backend.core.usecase.dto.OrderItemCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemRequestToOrderItemCommandConverter implements Converter<OrderItemRequest, OrderItemCommand> {

    @Override
    public OrderItemCommand convert(OrderItemRequest source) {
        return new OrderItemCommand(
                source.productId(),
                source.quantity()
        );
    }
}
