package com.ren.orderingSystem.ApiContracts.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantAddressInfoResponse {

    public long restaurantAddressId;
    public String country;
    public String city;
    public String streetName;
    public String streetAddress;
    public String pinCode;
    public String province;
}
