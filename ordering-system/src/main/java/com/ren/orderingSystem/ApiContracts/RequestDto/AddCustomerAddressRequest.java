package com.ren.orderingSystem.ApiContracts.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCustomerAddressRequest {

    @NotBlank(message = "Country cannot be null")
    private String country;
    @NotBlank(message = "City cannot be null")
    private String city;
    @NotBlank(message = "StreetAddress cannot be null")
    private String streetAddress1;

    private String streetAddress2;
    @NotBlank(message= "Pincode cannot be null")
    private String pinCode;
    @NotBlank(message = "Province cannot be null")
    private String province;
}
