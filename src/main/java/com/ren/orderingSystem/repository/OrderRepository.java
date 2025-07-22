package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findTopByCustomer_CustomerIdOrderByCreatedAtDesc(Long customerId);
}
