package com.ren.orderingSystem.ApiContracts.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderedItemsRequest {

    @NotBlank(message = "Quantity cannot be null")
    private long quantity;

    @NotBlank(message = "MenuItem id cannot be null")
    private long menuItemId;

}
