package com.ren.orderingSystem.ApiContracts.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class PlaceOrderRequest {

    private AddUserDetailForCustomerRequest addUserDetailForCustomerRequest;
    private List<OrderedItemsRequest> orderedItemsRequestsList;
}
