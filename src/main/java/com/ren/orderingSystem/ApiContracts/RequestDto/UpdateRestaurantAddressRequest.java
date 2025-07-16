package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateRestaurantAddressRequest {

    private String restaurantAddressId;
    private String country;
    private String city;
    private String streetName;
    private String StreetAddress;
    private String pinCode;
    private String province;

}
