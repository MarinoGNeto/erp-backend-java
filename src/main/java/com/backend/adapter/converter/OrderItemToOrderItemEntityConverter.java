package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import com.backend.adapter.outbound.persistence.entity.OrderItemEntity;
import com.backend.adapter.outbound.persistence.entity.ProductEntity;
import com.backend.core.domain.OrderItem;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemToOrderItemEntityConverter implements Converter<OrderItem, OrderItemEntity> {

    private final ConversionService conversionService;

    public OrderItemToOrderItemEntityConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public OrderItemEntity convert(OrderItem source) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setId(source.getId());
        orderItemEntity.setOrder(conversionService.convert(source.getOrder(), OrderEntity.class));
        orderItemEntity.setProduct(conversionService.convert(source.getProduct(), ProductEntity.class));
        orderItemEntity.setQuantity(source.getQuantity());
        orderItemEntity.setUnitPrice(source.getUnitPrice());

        return orderItemEntity;
    }
}
