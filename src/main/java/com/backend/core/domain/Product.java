package com.backend.core.domain;

import com.backend.core.domain.enums.ProductType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = -8059403921659304783L;

    private UUID id;
    private String name;
    private BigDecimal price;
    private ProductType type;
    private boolean active;

    public Product() {
        // Empty Constructor
    }

    public Product(UUID id, String name, BigDecimal price, ProductType type, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
