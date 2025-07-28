package com.backend.adapter.outbound.persistence.repository;

import com.backend.adapter.outbound.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID>, QuerydslPredicateExecutor<OrderItemEntity> {

    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.id = :itemId AND oi.order.id = :orderId")
    Optional<OrderItemEntity> findByIdAndOrderId(@Param("itemId") UUID itemId, @Param("orderId") UUID orderId);
}
