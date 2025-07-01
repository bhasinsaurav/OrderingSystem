package com.ren.orderingSystem.ApiContracts.WebSocketDto;

import com.ren.orderingSystem.repository.CustomerRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
public class IncludeCustomerInfo {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private IncludeCustomerAddressInfo includeCustomerAddressInfo;

}
