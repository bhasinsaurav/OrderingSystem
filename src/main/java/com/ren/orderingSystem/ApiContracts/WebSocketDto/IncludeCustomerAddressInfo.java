package com.ren.orderingSystem.ApiContracts.WebSocketDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class IncludeCustomerAddressInfo {

    private String country;
    private String city;
    private String streetAddress1;
    private String streetAddress2;
    private String pinCode;
    private String province;
}
