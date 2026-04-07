package com.example.order_service.dto;


import com.example.order_service.Enum.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private BigDecimal orderTotal;
    private OrderStatus orderStatus;
    private List<IncludeOrderItemsInfo> orderItemsInfo;
    // Note: includeCustomerInfo is left out here as it's added by the Orchestrator in the Monolith
}
