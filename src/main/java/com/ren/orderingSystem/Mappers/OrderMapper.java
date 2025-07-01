package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.Entity.Order;
import com.ren.orderingSystem.Entity.OrderItems;
import com.ren.orderingSystem.Enum.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public Order toOrderEntity(List<OrderItems> orderItems, Order order) {
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItems::getItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        return order;
    }
}
