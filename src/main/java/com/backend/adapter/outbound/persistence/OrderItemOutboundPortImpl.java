package com.backend.adapter.outbound.persistence;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import com.backend.adapter.outbound.persistence.entity.OrderItemEntity;
import com.backend.adapter.outbound.persistence.entity.QOrderItemEntity;
import com.backend.adapter.outbound.persistence.repository.OrderItemRepository;
import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import com.backend.core.port.OrderItemOutboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderItemCommand;
import com.querydsl.core.BooleanBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class OrderItemOutboundPortImpl implements OrderItemOutboundPort {

    private final ConversionService conversionService;
    private final OrderItemRepository orderItemRepository;

    public OrderItemOutboundPortImpl(ConversionService conversionService, OrderItemRepository orderItemRepository) {
        this.conversionService = conversionService;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem save(Order order, OrderItem orderItem) {
        OrderItemEntity itemEntity = Objects.requireNonNull(conversionService.convert(orderItem, OrderItemEntity.class));
        itemEntity.setOrder(Objects.requireNonNull(conversionService.convert(order, OrderEntity.class)));

        OrderItemEntity saved = orderItemRepository.save(itemEntity);

        return conversionService.convert(saved, OrderItem.class);
    }

    @Override
    public Optional<OrderItem> findByIdAndOrderId(UUID orderId, UUID orderItemId) {
        return orderItemRepository.findByIdAndOrderId(orderItemId, orderId)
                .map(entity -> Objects.requireNonNull(conversionService.convert(entity, OrderItem.class),
                        "Converted Order Item cannot be null"));
    }

    @Override
    public PageContent<OrderItem> findAll(UUID orderId, FilterOrderItemCommand filterOrderItemCommand, Integer pageSize, Integer pageNumber) {
        QOrderItemEntity qOrderItem = QOrderItemEntity.orderItemEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOrderItem.order.id.eq(orderId));

        if (Objects.nonNull(filterOrderItemCommand.productId())) {
            builder.and(qOrderItem.product.id.eq(filterOrderItemCommand.productId()));
        }

        if (Objects.nonNull(filterOrderItemCommand.quantity())) {
            builder.and(qOrderItem.quantity.eq(filterOrderItemCommand.quantity()));
        }

        if (Objects.nonNull(filterOrderItemCommand.unitPrice())) {
            builder.and(qOrderItem.unitPrice.eq(filterOrderItemCommand.unitPrice()));
        }

        Page<OrderItemEntity> pages = orderItemRepository.findAll(builder, PageRequest.of(pageNumber, pageSize));
        List<OrderItem> orderItemList = pages.getContent()
                .stream()
                .map(orderEntity -> conversionService.convert(orderEntity, OrderItem.class))
                .toList();

        return new PageContent<>(orderItemList, pages.getTotalPages(), pages.getTotalElements(), pages.getNumber());
    }

    @Override
    public OrderItem update(UUID orderId, UUID orderItemId, OrderItem orderItem) {
        OrderItemEntity itemEntity = orderItemRepository.save(Objects.requireNonNull(conversionService.convert(orderItem, OrderItemEntity.class), "Converted Order Item entity cannot be null"));
        return conversionService.convert(itemEntity, OrderItem.class);
    }
}
