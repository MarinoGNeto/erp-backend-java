package com.backend.adapter.converter;

import com.backend.adapter.outbound.persistence.entity.OrderItemEntity;
import com.backend.core.domain.OrderItem;
import com.backend.core.domain.Product;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityToOrderItemConverter implements Converter<OrderItemEntity, OrderItem> {

    private final ConversionService conversionService;

    public OrderItemEntityToOrderItemConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public OrderItem convert(OrderItemEntity source) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(source.getId());
        orderItem.setProduct(conversionService.convert(source.getProduct(),  Product.class));
        orderItem.setQuantity(source.getQuantity());
        orderItem.setUnitPrice(source.getUnitPrice());

        return orderItem;
    }
}
