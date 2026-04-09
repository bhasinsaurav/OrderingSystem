package com.ren.order_service.controller;


import com.ren.order_service.dto.OrderRequestToOrderService;
import com.ren.order_service.dto.OrderResponse;
import com.ren.order_service.dto.TotalOrdersResponse;
import com.ren.order_service.dto.UpdateStatusDto;
import com.ren.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequestToOrderService orderRequest) {
        OrderResponse response = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody UpdateStatusDto updateStatusDto) {
        orderService.updateStatus(updateStatusDto);
        return new ResponseEntity<>("Status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<TotalOrdersResponse> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        TotalOrdersResponse response = orderService.getOrdersByRestaurantId(restaurantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
