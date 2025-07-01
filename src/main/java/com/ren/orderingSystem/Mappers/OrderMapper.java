package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.PlaceOrderRequest;
import com.ren.orderingSystem.Entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrderEntity(PlaceOrderRequest placeOrderRequest, Order order){
        order.setTotalAmount(placeOrderRequest.getTotalAmount());
        return order;
    }
}
