package com.ren.orderingSystem.Controller.Restaurant;

import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateStatusDto;
import com.ren.orderingSystem.ApiContracts.ResponseDto.TotalOrdersResponse;
import com.ren.orderingSystem.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/restaurant/order")
public class RestaurantOrderController {

    private final OrderService orderService;

    public RestaurantOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/updateStatus/{customerUserId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable UUID customerUserId, @RequestBody UpdateStatusDto updateStatusDto) {
        orderService.updateStatus(updateStatusDto, customerUserId);
        return new ResponseEntity("Status updated successfully", HttpStatus.OK);
    }

    @GetMapping("/getOrders/{restaurantUserId}")
    public ResponseEntity<TotalOrdersResponse> getOrders(@PathVariable UUID restaurantUserId) {
        TotalOrdersResponse totalOrdersByUserId = orderService.getTotalOrdersByUserId(restaurantUserId);
        return new ResponseEntity<>(totalOrdersByUserId, HttpStatus.OK);

    }
}
