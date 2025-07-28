package com.backend.infrastructure;

import com.backend.adapter.converter.OrderEntityToOrderConverter;
import com.backend.adapter.converter.OrderItemEntityToOrderItemConverter;
import com.backend.adapter.converter.OrderItemToOrderItemEntityConverter;
import com.backend.adapter.converter.OrderRequestToOrderCommandConverter;
import com.backend.adapter.converter.OrderToOrderEntityConverter;
import com.backend.adapter.converter.OrderToOrderResponseConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterWebMvcConfiguration implements WebMvcConfigurer {
    
    private final ConversionService conversionService;

    public ConverterWebMvcConfiguration(@Lazy ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OrderRequestToOrderCommandConverter(conversionService));
        registry.addConverter(new OrderToOrderResponseConverter(conversionService));
        registry.addConverter(new OrderItemToOrderItemEntityConverter(conversionService));
        registry.addConverter(new OrderToOrderEntityConverter(conversionService));
        registry.addConverter(new OrderEntityToOrderConverter(conversionService));
        registry.addConverter(new OrderItemEntityToOrderItemConverter(conversionService));
    }
}
