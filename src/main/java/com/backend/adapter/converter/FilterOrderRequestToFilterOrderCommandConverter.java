package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.FilterOrderRequest;
import com.backend.core.usecase.dto.FilterOrderCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterOrderRequestToFilterOrderCommandConverter implements Converter<FilterOrderRequest, FilterOrderCommand> {

    @Override
    public FilterOrderCommand convert(FilterOrderRequest source) {
        return new FilterOrderCommand(
                source.status(),
                source.discountPercentage()
        );
    }
}
