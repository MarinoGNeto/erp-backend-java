package com.backend.core.port;

import com.backend.core.domain.Order;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderCommand;

import java.util.Optional;
import java.util.UUID;

public interface OrderPersistenceOutboundPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    PageContent<Order> findAll(FilterOrderCommand filterOrderCommand, Integer pageSize, Integer pageNumber);

    Order update(Order order);

    void deleteById(UUID id);

    boolean existsById(UUID id);

}
