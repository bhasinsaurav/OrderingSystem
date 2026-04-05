package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddCustomerAddressRequest;
import com.ren.orderingSystem.Entity.Customer;
import com.ren.orderingSystem.Entity.CustomerAddress;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CustomerAddressMapper {

    public CustomerAddress toCustomerAddressEntity(AddCustomerAddressRequest addCustomerAddressRequest, Customer customer){
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCountry(addCustomerAddressRequest.getCountry());
        customerAddress.setCity(addCustomerAddressRequest.getCity());
        customerAddress.setStreetAddress1(addCustomerAddressRequest.getStreetAddress1());
        customerAddress.setStreetAddress2(addCustomerAddressRequest.getStreetAddress2());
        customerAddress.setPinCode(addCustomerAddressRequest.getPinCode());
        customerAddress.setProvince(addCustomerAddressRequest.getProvince());
        customerAddress.setCustomer(customer);
        return customerAddress;
    }
}
