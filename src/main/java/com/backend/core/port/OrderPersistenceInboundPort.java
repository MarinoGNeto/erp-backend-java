package com.backend.core.port;

import com.backend.core.domain.Order;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderCommand;
import com.backend.core.usecase.dto.OrderCommand;

import java.util.UUID;

public interface OrderPersistenceInboundPort {

    Order create(OrderCommand orderCommand);

    Order findById(UUID id);

    PageContent<Order> findAll(FilterOrderCommand filterOrderCommand, Integer pageSize, Integer pageNumber);

    Order update(OrderCommand orderCommand, UUID id);

    void deleteById(UUID id);
}
