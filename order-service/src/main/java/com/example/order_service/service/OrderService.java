package com.example.order_service.service;

import com.example.order_service.dto.OrderRequestToOrderService;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.TotalOrdersResponse;
import com.example.order_service.dto.UpdateStatusDto;

public interface OrderService {

    public OrderResponse placeOrder(OrderRequestToOrderService orderRequest);

    public void updateStatus(UpdateStatusDto updateStatusDto);

    public TotalOrdersResponse getOrdersByRestaurantId(Long restaurantId);
}
