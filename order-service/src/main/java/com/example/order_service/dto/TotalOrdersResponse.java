package com.example.order_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class TotalOrdersResponse {
    private List<OrderResponse> allOrders;
}
