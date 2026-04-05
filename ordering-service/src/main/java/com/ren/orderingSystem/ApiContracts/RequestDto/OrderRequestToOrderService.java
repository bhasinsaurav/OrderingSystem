package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestToOrderService {

    private long customerId;
    private long restaurantId;
    private List<OrderedItemsRequest> orderedItemsRequests;
}
