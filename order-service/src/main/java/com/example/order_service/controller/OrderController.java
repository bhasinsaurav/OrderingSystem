package com.example.order_service.controller;

import com.example.order_service.dto.*;
import com.example.order_service.service.impl.OrderServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequestToOrderService orderRequest) {
        OrderResponse response = orderServiceImpl.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody UpdateStatusDto updateStatusDto) {
        orderServiceImpl.updateStatus(updateStatusDto);
        return new ResponseEntity<>("Status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<TotalOrdersResponse> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        TotalOrdersResponse response = orderServiceImpl.getOrdersByRestaurantId(restaurantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
