package com.example.order_service.service.impl;

import com.example.order_service.Enum.OrderStatus;
import com.example.order_service.dto.*;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItems;

import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequestToOrderService orderRequest) {
        List<OrderItems> orderItemsList = new ArrayList<>();
        BigDecimal orderTotal = BigDecimal.ZERO;

        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setRestaurantId(orderRequest.getRestaurantId());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        for (OrderedItemsRequest itemRequest : orderRequest.getOrderedItemsRequests()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setMenuItemName(itemRequest.getMenuItemName());
            orderItem.setItemPrice(itemRequest.getMenuItemPrice());
            orderItem.setMenuItemId(itemRequest.getMenuItemId());
            orderItem.setQuantity(itemRequest.getQuantity());
            
            BigDecimal itemTotal = itemRequest.getMenuItemPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            orderItem.setItemTotalPrice(itemTotal);
            orderTotal = orderTotal.add(itemTotal);
            
            orderItem.setOrder(order);
            orderItemsList.add(orderItem);
        }

        order.setOrderItems(orderItemsList);
        order.setTotalAmount(orderTotal);

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    @Transactional
    public void updateStatus(UpdateStatusDto updateStatusDto) {
        Order order = orderRepository.findById(updateStatusDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String status = updateStatusDto.getOrderStatus();
        switch (status) {
            case "Accepted" -> order.setOrderStatus(OrderStatus.ACCEPTED);
            case "Rejected" -> order.setOrderStatus(OrderStatus.REJECTED);
            case "Prepared" -> order.setOrderStatus(OrderStatus.PREPARED);
            case "Delivered" -> order.setOrderStatus(OrderStatus.DELIVERED);
            case "Cancelled" -> order.setOrderStatus(OrderStatus.CANCELED);
        }
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public TotalOrdersResponse getOrdersByRestaurantId(Long restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        List<OrderResponse> orderResponses = orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
        
        TotalOrdersResponse response = new TotalOrdersResponse();
        response.setAllOrders(orderResponses);
        return response;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setOrderTotal(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        
        List<IncludeOrderItemsInfo> itemInfos = order.getOrderItems().stream().map(item -> {
            IncludeOrderItemsInfo info = new IncludeOrderItemsInfo();
            info.setItemName(item.getMenuItemName());
            info.setItemPrice(item.getItemPrice());
            info.setQuantity(item.getQuantity());
            info.setItemTotalPrice(item.getItemTotalPrice());
            return info;
        }).collect(Collectors.toList());
        
        response.setOrderItemsInfo(itemInfos);
        return response;
    }
}
