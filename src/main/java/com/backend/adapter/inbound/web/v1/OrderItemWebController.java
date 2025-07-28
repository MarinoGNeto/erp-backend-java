package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.FilterOrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderItemResponse;
import com.backend.adapter.inbound.web.dto.Pagination;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface OrderItemWebController {

    String PATH = "/api/orders/{orderId}/items";
    String ID_PATH = "/{itemId}";
    String ORDER_ID_PARAMETER_MESSAGE = "Order unique ID";
    String ITEM_ID_PARAMETER_MESSAGE = "Item unique ID";

    @PostMapping(path = PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderItemResponse> create(@PathVariable("orderId") UUID orderId,
                                @Valid @RequestBody OrderItemRequest orderItemRequest);

    @PutMapping(path = PATH + ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderItemResponse> update(@Parameter(description = ORDER_ID_PARAMETER_MESSAGE, required = true) @PathVariable("orderId") UUID orderId,
                                             @Parameter(description = ITEM_ID_PARAMETER_MESSAGE, required = true) @PathVariable("itemId") UUID itemId,
                                             @Valid @RequestBody OrderItemRequest orderItemRequest);

    @GetMapping(path = PATH + ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OrderItemResponse> findById(@Parameter(description = ORDER_ID_PARAMETER_MESSAGE, required = true) @PathVariable("orderId") UUID orderId,
                                           @Parameter(description = ITEM_ID_PARAMETER_MESSAGE, required = true) @PathVariable("itemId") UUID itemId);

    @GetMapping(path = PATH + ":paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Pagination<OrderItemResponse>> findAll(
            @Min(value = 10, message = "Page size must be at least 10.") @Max(value = 100, message = "Page size must limited to 100.") @RequestParam(name = "_size", value = "_size", defaultValue = "10", required = false) Integer pageSize,
            @Min(value = 0, message = "Page number must be 0 or greater.") @RequestParam(name = "_page", value = "_page", defaultValue = "0", required = false) Integer page,
            @Parameter(description = ORDER_ID_PARAMETER_MESSAGE, required = true) @PathVariable("orderId") UUID orderId,
            @Valid FilterOrderItemRequest filterOrderItemRequest);

    @DeleteMapping(path = PATH + ID_PATH)
    ResponseEntity<Void> delete(@Parameter(description = ORDER_ID_PARAMETER_MESSAGE, required = true) @PathVariable("orderId") UUID orderId,
                                @Parameter(description = ITEM_ID_PARAMETER_MESSAGE, required = true) @PathVariable("itemId") UUID itemId);





}
