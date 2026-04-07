package com.example.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderedItemsRequest {

    @NotBlank(message = "Quantity cannot be null")
    private long quantity;

    @NotBlank(message = "MenuItem id cannot be null")
    private long menuItemId;

    @NotBlank(message = "MenuItem name cannot be null")
    private String menuItemName;

    @NotBlank(message = "MenuItem Price cannot be null")
    private BigDecimal menuItemPrice;

}
