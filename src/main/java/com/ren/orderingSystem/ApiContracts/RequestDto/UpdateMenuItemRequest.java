package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateMenuItemRequest {

    private Long menuItemId;
    private String description;
    private BigDecimal price;
    private String category;
    private String itemName;
    private Boolean isAvailable;

}
