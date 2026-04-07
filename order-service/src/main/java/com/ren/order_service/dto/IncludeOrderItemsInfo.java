package com.ren.order_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class IncludeOrderItemsInfo {
    private String itemName;
    private long quantity;
    private BigDecimal itemPrice;
    private BigDecimal itemTotalPrice;
}
