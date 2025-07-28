package com.backend.adapter.outbound.persistence.repository;

import com.backend.adapter.outbound.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID>, QuerydslPredicateExecutor<OrderEntity> {

}
