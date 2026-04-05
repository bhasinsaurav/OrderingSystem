package com.ren.orderingSystem.repository;

import com.ren.orderingSystem.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface OrderRepository extends JpaRepository<Order, Long> {
    

    Order getOrderByOrderId(long orderId);

    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByCustomerId(Long customerId);
}
