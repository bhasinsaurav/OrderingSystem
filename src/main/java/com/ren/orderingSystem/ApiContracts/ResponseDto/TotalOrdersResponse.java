package com.ren.orderingSystem.ApiContracts.ResponseDto;

import com.ren.orderingSystem.ApiContracts.WebSocketDto.OrderResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TotalOrdersResponse {

    private List<OrderResponse> allOrders;
}
