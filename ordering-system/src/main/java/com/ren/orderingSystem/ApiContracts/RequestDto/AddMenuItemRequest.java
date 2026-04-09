package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AddMenuItemRequest {

    private String description;
    private BigDecimal price;
    private String category;
    private String itemName;
    private Boolean isAvailable;

}
