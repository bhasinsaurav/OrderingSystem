package com.ren.orderingSystem.Controller.Customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerOrderController {

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(){

    }
}
