package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.OrderItemResponse;
import com.backend.adapter.inbound.web.dto.OrderResponse;
import com.backend.core.domain.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderToOrderResponseConverter implements Converter<Order, OrderResponse> {

    private final ConversionService conversionService;

    public OrderToOrderResponseConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public OrderResponse convert(Order source) {
        List<OrderItemResponse> items = source.getItems().stream()
                .map(item -> conversionService.convert(item, OrderItemResponse.class))
                .toList();

        return new OrderResponse(
            source.getId(),
            source.getStatus(),
            source.getDiscountPercentage(),
            source.getTotal(),
            items,
            source.getCreatedAt()
        );
    }
}
