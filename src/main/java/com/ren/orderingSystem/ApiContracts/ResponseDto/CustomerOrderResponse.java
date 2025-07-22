package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;

@Data
public class CustomerOrderResponse {

    private long OrderId;
    private String OrderStatus;
}
