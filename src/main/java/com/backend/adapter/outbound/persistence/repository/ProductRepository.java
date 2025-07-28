package com.backend.adapter.outbound.persistence.repository;

import com.backend.adapter.outbound.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, QuerydslPredicateExecutor<ProductEntity> {

    @Query("SELECT COUNT(oi) > 0 FROM OrderItemEntity oi WHERE oi.product.id = :productId")
    boolean isProductUsedInOrders(@Param("productId") UUID productId);

}
