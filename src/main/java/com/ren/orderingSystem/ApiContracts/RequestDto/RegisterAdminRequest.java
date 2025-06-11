package com.ren.orderingSystem.ApiContracts.RequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterAdminRequest {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private AddRestaurantDetailsRequest restaurantDetails;
}
