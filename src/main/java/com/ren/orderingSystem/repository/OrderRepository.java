package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findTopByCustomer_CustomerIdOrderByCreatedAtDesc(Long customerId);

    Order getOrderByOrderId(long orderId);
}
