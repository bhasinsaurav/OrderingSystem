package com.example.order_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStatusDto {
    private long orderId;
    private String orderStatus;
}
