package com.ren.orderingSystem.ApiContracts.ResponseDto;

import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import com.ren.orderingSystem.Entity.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TotalOrdersResponse {

    private List<Order> allOrders;
}
