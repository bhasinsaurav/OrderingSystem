package com.ren.orderingSystem.ApiContracts.WebSocketDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class SendOrderToRestaurant {

    private BigDecimal orderTotal;
    private IncludeCustomerInfo includeCustomerInfo;
    private List<IncludeOrderItemsInfo> orderItemsInfo;
}
