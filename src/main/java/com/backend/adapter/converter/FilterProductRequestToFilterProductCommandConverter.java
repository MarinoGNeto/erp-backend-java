package com.backend.adapter.converter;

import com.backend.adapter.inbound.web.dto.FilterProductRequest;
import com.backend.core.usecase.dto.FilterProductCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterProductRequestToFilterProductCommandConverter implements Converter<FilterProductRequest, FilterProductCommand> {

    @Override
    public FilterProductCommand convert(FilterProductRequest source) {
        return new FilterProductCommand(
                source.name(),
                source.type(),
                source.active()
        );
    }
}
