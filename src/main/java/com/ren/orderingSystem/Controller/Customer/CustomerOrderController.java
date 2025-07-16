package com.ren.orderingSystem.Controller.Customer;

import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customer")

public class CustomerOrderController {

    private final OrderService orderService;

    @Autowired
    public CustomerOrderController(OrderService orderService){
        this.orderService = orderService;
    }
    @PostMapping("/place-order/{userId}")
    public ResponseEntity<?> placeOrder(@PathVariable UUID userId, @RequestBody PlaceOrderRequest placeOrderRequest){
        UUID customerId = orderService.placeOrder(placeOrderRequest, userId);
        return new ResponseEntity<>(customerId, HttpStatus.OK);
    }
}
