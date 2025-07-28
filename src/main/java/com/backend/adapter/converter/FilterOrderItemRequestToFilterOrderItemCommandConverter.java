package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.FilterOrderItemRequest;
import com.backend.core.usecase.dto.FilterOrderItemCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterOrderItemRequestToFilterOrderItemCommandConverter implements Converter<FilterOrderItemRequest, FilterOrderItemCommand> {

    @Override
    public FilterOrderItemCommand convert(FilterOrderItemRequest source) {
        return new FilterOrderItemCommand(
                source.productId(),
                source.quantity(),
                source.unitPrice()
        );
    }
}
