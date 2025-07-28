package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEntityToOrderConverter implements Converter<OrderEntity, Order> {

    private final ConversionService conversionService;

    public OrderEntityToOrderConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Order convert(OrderEntity source) {
        List<OrderItem> items = source.getItems().stream()
                .map(item -> conversionService.convert(item, OrderItem.class))
                .toList();

        Order order = new Order();
        order.setId(source.getId());
        order.setStatus(source.getStatus());
        order.setDiscountPercentage(source.getDiscountPercentage());
        order.setItems(items);
        order.setTotal(source.getTotal());
        order.setCreatedAt(source.getCreatedAt());

        return order;
    }
}
