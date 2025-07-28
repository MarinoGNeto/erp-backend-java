package com.backend.adapter.outbound.persistence;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import com.backend.adapter.outbound.persistence.entity.QOrderEntity;
import com.backend.adapter.outbound.persistence.repository.OrderRepository;
import com.backend.core.domain.Order;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderCommand;
import com.querydsl.core.BooleanBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class OrderOutboundPortImpl implements OrderOutboundPort {

    private final ConversionService conversionService;
    private final OrderRepository orderRepository;

    public OrderOutboundPortImpl(ConversionService conversionService, OrderRepository orderRepository) {
        this.conversionService = conversionService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = Objects.requireNonNull(conversionService.convert(order, OrderEntity.class), "Converted Order entity cannot be null");
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));

        OrderEntity saved = orderRepository.save(orderEntity);

        return conversionService.convert(saved, Order.class);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);

        return optionalOrder.map(orderEntity -> Objects.requireNonNull(conversionService.convert(orderEntity, Order.class),
                "Converted Order cannot be null"));

    }

    @Override
    public PageContent<Order> findAll(FilterOrderCommand filterOrderCommand, Integer pageSize, Integer pageNumber) {
        QOrderEntity qOrder = QOrderEntity.orderEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if (Objects.nonNull(filterOrderCommand.status())) {
            builder.and(qOrder.status.eq(filterOrderCommand.status()));
        }

        if (Objects.nonNull(filterOrderCommand.discountPercentage())) {
            builder.and(qOrder.discountPercentage.eq(filterOrderCommand.discountPercentage()));
        }

        Page<OrderEntity> pages = orderRepository.findAll(builder, PageRequest.of(pageNumber, pageSize));
        List<Order> orderList = pages.getContent()
                .stream()
                .map(orderEntity -> conversionService.convert(orderEntity, Order.class))
                .toList();

        return new PageContent<>(orderList, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber());
    }

    @Override
    public Order update(Order order) {
        OrderEntity orderEntity = Objects.requireNonNull(conversionService.convert(order, OrderEntity.class), "Converted Order entity cannot be null");
        orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        return conversionService.convert(savedOrder, Order.class);
    }

    @Override
    public void deleteById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return orderRepository.existsById(id);
    }
}
