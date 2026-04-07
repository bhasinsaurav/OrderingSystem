package com.ren.order_service.service;

import com.ren.order_service.dto.OrderRequestToOrderService;
import com.ren.order_service.dto.OrderResponse;
import com.ren.order_service.dto.TotalOrdersResponse;
import com.ren.order_service.dto.UpdateStatusDto;

public interface OrderService {

    public OrderResponse placeOrder(OrderRequestToOrderService orderRequest);

    public void updateStatus(UpdateStatusDto updateStatusDto);

    public TotalOrdersResponse getOrdersByRestaurantId(Long restaurantId);
}
