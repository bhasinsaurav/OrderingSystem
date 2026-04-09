package com.ren.orderingSystem.ApiContracts.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddUserDetailForCustomerRequest {

    @NotBlank(message = "Firstname cannot be blank")
    private String firstName;
    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    private AddCustomerDetailRequest addCustomerDetailRequest;
}
