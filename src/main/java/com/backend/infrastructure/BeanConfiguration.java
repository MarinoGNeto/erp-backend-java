package com.backend.infrastructure;

import com.backend.adapter.outbound.persistence.OrderItemOutboundPortImpl;
import com.backend.adapter.outbound.persistence.OrderOutboundPortImpl;
import com.backend.adapter.outbound.persistence.ProductOutboundPortImpl;
import com.backend.adapter.outbound.persistence.repository.OrderItemRepository;
import com.backend.adapter.outbound.persistence.repository.OrderRepository;
import com.backend.adapter.outbound.persistence.repository.ProductRepository;
import com.backend.core.port.OrderInboundPort;
import com.backend.core.port.OrderItemInboundPort;
import com.backend.core.port.OrderItemOutboundPort;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.port.ProductInboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.OrderItemUseCaseImpl;
import com.backend.core.usecase.OrderUseCaseImpl;
import com.backend.core.usecase.ProductUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductInboundPort productInboundPort(ProductOutboundPort productOutboundPort) {
        return new ProductUseCaseImpl(productOutboundPort);
    }

    @Bean
    public ProductOutboundPort productOutboundPort(ConversionService conversionService, ProductRepository productRepository) {
        return new ProductOutboundPortImpl(conversionService,  productRepository);
    }

    @Bean
    public OrderInboundPort orderInboundPort(OrderOutboundPort orderOutboundPort, ProductOutboundPort productOutboundPort) {
        return new OrderUseCaseImpl(orderOutboundPort, productOutboundPort);
    }

    @Bean
    public OrderOutboundPort orderOutboundPort(ConversionService conversionService, OrderRepository orderRepository) {
        return new OrderOutboundPortImpl(conversionService, orderRepository);
    }

    @Bean
    public OrderItemInboundPort orderItemInboundPort(OrderItemOutboundPort orderItemOutboundPort, ProductOutboundPort productOutboundPort, OrderOutboundPort orderOutboundPort) {
        return new OrderItemUseCaseImpl(orderItemOutboundPort, productOutboundPort, orderOutboundPort);
    }

    @Bean
    public OrderItemOutboundPort orderItemOutboundPort(ConversionService conversionService, OrderItemRepository  orderItemRepository) {
        return new OrderItemOutboundPortImpl(conversionService, orderItemRepository);
    }
}
