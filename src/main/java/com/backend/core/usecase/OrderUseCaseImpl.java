package com.backend.core.usecase;

import com.backend.core.domain.Order;
import com.backend.core.domain.OrderItem;
import com.backend.core.domain.Product;
import com.backend.core.domain.enums.OrderStatus;
import com.backend.core.domain.enums.ProductType;
import com.backend.core.port.OrderInboundPort;
import com.backend.core.port.OrderOutboundPort;
import com.backend.core.port.ProductOutboundPort;
import com.backend.core.usecase.dto.FilterOrderCommand;
import com.backend.core.usecase.dto.OrderCommand;
import com.backend.core.usecase.dto.OrderItemCommand;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderUseCaseImpl implements OrderInboundPort {

    private static final String ORDER_NOT_FOUND_WITH_ID_MESSAGE = "Order not found with id: [%s]";
    private static final String PRODUCT_NOT_FOUND_WITH_ID_MESSAGE = "Product not found with id: [%s]";
    private static final String PRODUCT_INACTIVE_MESSAGE = "Cannot add inactive product with id: [%s]";

    private final OrderOutboundPort orderOutboundPort;
    private final ProductOutboundPort productOutboundPort;

    public OrderUseCaseImpl(OrderOutboundPort orderOutboundPort, ProductOutboundPort productOutboundPort) {
        this.orderOutboundPort = orderOutboundPort;
        this.productOutboundPort = productOutboundPort;
    }

    @Override
    @Transactional
    public Order create(OrderCommand orderCommand) {
        Order order = new Order();
        order.setDiscountPercentage(orderCommand.discountPercentage());
        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());

        order.setItems(orderCommand.items()
                .stream()
                .map(this::toOrderItem)
                .toList());

        order.setTotal(calculateTotalWithDiscount(order));

        return orderOutboundPort.save(order);
    }

    @Override
    public Order findById(UUID id) {
        return findOrderById(id);
    }

    @Override
    public PageContent<Order> findAll(FilterOrderCommand filterOrderCommand, Integer pageSize, Integer pageNumber) {
        return orderOutboundPort.findAll(filterOrderCommand, pageSize, pageNumber);
    }

    @Override
    @Transactional
    public Order update(OrderCommand orderCommand, UUID id) {
        Order order = findOrderById(id);

        order.setDiscountPercentage(orderCommand.discountPercentage());
        order.setItems(orderCommand.items()
                .stream()
                .map(this::toOrderItem)
                .toList());

        order.setTotal(calculateTotalWithDiscount(order));

        return orderOutboundPort.update(order);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!orderOutboundPort.existsById(id)) {
            throw new EntityNotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MESSAGE, id));
        }

        orderOutboundPort.deleteById(id);
    }

    private Order findOrderById(UUID id) {
        return orderOutboundPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ORDER_NOT_FOUND_WITH_ID_MESSAGE, id)));
    }

    private Product findActiveProductById(UUID productId) {
        Product product = productOutboundPort.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MESSAGE, productId)));

        if (!product.isActive()) {
            throw new IllegalArgumentException(String.format(PRODUCT_INACTIVE_MESSAGE, productId));
        }

        return product;
    }

    private OrderItem toOrderItem(OrderItemCommand command) {
        Product product = findActiveProductById(command.productId());

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(command.quantity());
        item.setUnitPrice(product.getPrice());
        return item;
    }

    private BigDecimal calculateTotalWithDiscount(Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal productTotal = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);

            if (item.getProduct().getType() == ProductType.PRODUCT) {
                productTotal = productTotal.add(itemTotal);
            }
        }

        if (order.getStatus() != OrderStatus.OPEN) {
            return subtotal;
        }

        BigDecimal discount = productTotal
                .multiply(order.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        return subtotal.subtract(discount);
    }
}
