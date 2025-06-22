package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RestaurantLoginResponse {

    private Map<String, String> loginResponse;
}
