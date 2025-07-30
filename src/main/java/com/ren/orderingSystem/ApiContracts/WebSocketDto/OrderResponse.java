package com.ren.orderingSystem.ApiContracts.WebSocketDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {

    private BigDecimal orderTotal;
    private IncludeCustomerInfo includeCustomerInfo;
    private List<IncludeOrderItemsInfo> orderItemsInfo;
    private Long orderId;
    private String orderStatus;
}
