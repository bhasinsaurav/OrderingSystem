package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class AddRestaurantDetailsRequest {

    private String contactNumber;
    private String restaurantName;
    private String slug;
    private Set<AddRestaurantAddressRequest> restaurantAddresses;
}
