package com.ren.orderingSystem.Feign;

import com.ren.orderingSystem.ApiContracts.RequestDto.OrderRequestToOrderService;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateStatusDto;
import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.ApiContracts.ResponseDto.TotalOrdersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "http://localhost:8081")
public interface OrderFeignClient {

    @PostMapping("/order/create")
    OrderResponse placeOrder(@RequestBody OrderRequestToOrderService orderRequest);

    @PutMapping("/order/updateStatus")
    void updateStatus(@RequestBody UpdateStatusDto updateStatusDto);

    @GetMapping("/order/restaurant/{restaurantId}")
    TotalOrdersResponse getOrdersByRestaurant(@PathVariable("restaurantId") Long restaurantId);
}
