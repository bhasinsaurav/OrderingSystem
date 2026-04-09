package com.ren.orderingSystem.ApiContracts.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class AddCustomerDetailRequest {


    private Set<AddCustomerAddressRequest> customerAddressRequestList;

}
