package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.FilterOrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderItemResponse;
import com.backend.adapter.inbound.web.dto.Pagination;
import com.backend.core.domain.OrderItem;
import com.backend.core.port.OrderItemInboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterOrderItemCommand;
import com.backend.core.usecase.dto.OrderItemCommand;
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
public class OrderItemWebControllerImpl implements OrderItemWebController {

    private final OrderItemInboundPort orderItemInboundPort;
    private final ConversionService conversionService;

    public OrderItemWebControllerImpl(OrderItemInboundPort orderItemInboundPort, ConversionService conversionService) {
        this.orderItemInboundPort = orderItemInboundPort;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<OrderItemResponse> create(UUID orderId, OrderItemRequest orderItemRequest) {
        OrderItemCommand orderItemCommand = conversionService.convert(orderItemRequest, OrderItemCommand.class);

        OrderItemResponse orderItemResponse = conversionService.convert(orderItemInboundPort.create(orderId, orderItemCommand), OrderItemResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemResponse);
    }

    @Override
    public ResponseEntity<OrderItemResponse> update(UUID orderId, UUID itemId, OrderItemRequest orderItemRequest) {
        OrderItemCommand orderCommand = conversionService.convert(orderItemRequest, OrderItemCommand.class);
        OrderItem orderItem = orderItemInboundPort.update(orderCommand, orderId, itemId);

        OrderItemResponse orderItemResponse = conversionService.convert(orderItem, OrderItemResponse.class);
        return ResponseEntity.ok(orderItemResponse);
    }

    @Override
    public ResponseEntity<OrderItemResponse> findById(UUID orderId, UUID itemId) {
        return ResponseEntity.ok(conversionService.convert(orderItemInboundPort.findByIdAndOrderId(orderId, itemId), OrderItemResponse.class));
    }

    @Override
    public ResponseEntity<Pagination<OrderItemResponse>> findAll(Integer pageSize, Integer page, UUID orderId, FilterOrderItemRequest filterOrderItemRequest) {
        PageContent<OrderItem> pageContent = orderItemInboundPort.findAll(orderId, conversionService.convert(filterOrderItemRequest, FilterOrderItemCommand.class), pageSize, page);
        List<OrderItemResponse> orderItemResponseList = pageContent.content().stream().map(orderItem -> conversionService.convert(orderItem, OrderItemResponse.class)).toList();
        Pagination<OrderItemResponse> responsePagination = new Pagination<>(orderItemResponseList, pageContent.totalPages(), pageContent.totalElements(), pageContent.pageNumber());

        return ResponseEntity.ok(responsePagination);
    }

    @Override
    public ResponseEntity<Void> delete(UUID orderId, UUID itemId) {
        orderItemInboundPort.deleteById(orderId, itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
