package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminLoginRequest {

    private String userName;
    private String password;
}
