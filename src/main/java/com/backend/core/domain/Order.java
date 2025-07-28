package com.backend.core.domain;

import com.backend.core.domain.enums.OrderStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 6515539355001683071L;

    private UUID id;
    private OrderStatus status;
    private BigDecimal discountPercentage;
    private List<OrderItem> items;
    private BigDecimal total;
    private LocalDateTime createdAt;

    public Order() {
        // Empty
    }

    public Order(UUID id, OrderStatus status, BigDecimal discountPercentage, List<OrderItem> items, BigDecimal total, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.discountPercentage = discountPercentage;
        this.items = items;
        this.total = total;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
