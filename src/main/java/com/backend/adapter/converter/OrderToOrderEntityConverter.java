package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import com.backend.adapter.outbound.persistence.entity.OrderItemEntity;
import com.backend.core.domain.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderToOrderEntityConverter implements Converter<Order, OrderEntity> {

    private final ConversionService conversionService;

    public OrderToOrderEntityConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public OrderEntity convert(Order source) {
        List<OrderItemEntity> items = source.getItems().stream()
                .map(item -> conversionService.convert(item, OrderItemEntity.class))
                .toList();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(source.getId());
        orderEntity.setStatus(source.getStatus());
        orderEntity.setDiscountPercentage(source.getDiscountPercentage());
        orderEntity.setItems(items);
        orderEntity.setTotal(source.getTotal());
        orderEntity.setCreatedAt(source.getCreatedAt());

        return orderEntity;
    }
}
