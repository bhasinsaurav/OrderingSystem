package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStatusDto {

    private String orderStatus;
    private Long orderId;
}
