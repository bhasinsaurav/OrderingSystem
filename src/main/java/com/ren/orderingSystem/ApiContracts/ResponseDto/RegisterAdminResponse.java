package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class RegisterAdminResponse {

    private UUID userId;

}
