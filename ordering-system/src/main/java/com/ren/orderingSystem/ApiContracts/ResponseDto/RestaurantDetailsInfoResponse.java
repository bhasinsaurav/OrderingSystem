package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RestaurantDetailsInfoResponse {
    public String restaurantName;
    public String contactNumber;
    public String slug;
    public Set<RestaurantAddressInfoResponse> restaurantAddressInfoResponses;
}
