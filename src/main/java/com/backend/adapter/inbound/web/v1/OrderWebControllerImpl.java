package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.FilterOrderRequest;
import com.backend.adapter.inbound.web.dto.OrderRequest;
import com.backend.adapter.inbound.web.dto.OrderResponse;
import com.backend.adapter.inbound.web.dto.Pagination;
import com.backend.core.domain.Order;
import com.backend.core.port.OrderInboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderCommand;
import com.backend.core.usecase.dto.OrderCommand;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@Validated
public class OrderWebControllerImpl implements OrderWebController {

    private final OrderInboundPort orderInboundPort;
    private final ConversionService conversionService;

    public OrderWebControllerImpl(OrderInboundPort orderInboundPort, ConversionService conversionService) {
        this.orderInboundPort = orderInboundPort;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<OrderResponse> create(OrderRequest orderRequest) {
        OrderCommand orderCommand = conversionService.convert(orderRequest, OrderCommand.class);

        OrderResponse orderResponse = conversionService.convert(orderInboundPort.create(orderCommand), OrderResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @Override
    public ResponseEntity<OrderResponse> update(UUID id, OrderRequest orderRequest) {
        OrderCommand orderCommand = conversionService.convert(orderRequest, OrderCommand.class);
        Order order = orderInboundPort.update(orderCommand, id);

        OrderResponse orderResponse = conversionService.convert(order, OrderResponse.class);
        return ResponseEntity.ok(orderResponse);
    }

    @Override
    public ResponseEntity<OrderResponse> findById(UUID id) {
        return ResponseEntity.ok(conversionService.convert(orderInboundPort.findById(id), OrderResponse.class));
    }

    @Override
    public ResponseEntity<Pagination<OrderResponse>> findAll(Integer pageSize, Integer page, FilterOrderRequest filterOrderRequest) {
        PageContent<Order> pageContent = orderInboundPort.findAll(conversionService.convert(filterOrderRequest, FilterOrderCommand.class), pageSize, page);
        List<OrderResponse> orderResponseList = pageContent.content().stream().map(order -> conversionService.convert(order, OrderResponse.class)).toList();
        Pagination<OrderResponse> responsePagination = new Pagination<>(orderResponseList, pageContent.totalPages(), pageContent.totalElements(), pageContent.pageNumber());

        return ResponseEntity.ok(responsePagination);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        orderInboundPort.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
