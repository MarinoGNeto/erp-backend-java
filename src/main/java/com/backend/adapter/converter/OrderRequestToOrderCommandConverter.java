package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.OrderRequest;
import com.backend.core.usecase.dto.OrderCommand;
import com.backend.core.usecase.dto.OrderItemCommand;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderRequestToOrderCommandConverter implements Converter<OrderRequest, OrderCommand> {

    private final ConversionService conversionService;

    public OrderRequestToOrderCommandConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public OrderCommand convert(OrderRequest source) {
        List<OrderItemCommand> items = source.items().stream()
                .map(item -> conversionService.convert(item, OrderItemCommand.class))
                .toList();

        return new OrderCommand(
                source.discountPercentage(),
                items
        );
    }
}
