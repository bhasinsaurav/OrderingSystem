package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AddRestaurantAddressRequest {

    private String country;
    private String city;
    private String streetName;
    private String streetAddress;
    private String pinCode;
    private String province;
}