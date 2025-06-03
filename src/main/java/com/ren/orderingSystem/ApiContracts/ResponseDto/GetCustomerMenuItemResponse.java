package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GetCustomerMenuItemResponse {

    private long itemId;
    private String description;
    private BigDecimal price;
    private String category;
    private String itemName;
    private Boolean isAvailable;


}
