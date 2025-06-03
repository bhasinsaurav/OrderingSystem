package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantAddressRequest;
import com.ren.orderingSystem.Entity.RestaurantAddress;
import org.springframework.stereotype.Component;

@Component
public class RestaurantAddressMapper {

    public static RestaurantAddress toEntity(AddRestaurantAddressRequest newRestaurantAddress) {
        RestaurantAddress restaurantAddress = new RestaurantAddress();
        restaurantAddress.setCountry(newRestaurantAddress.getCountry());
        restaurantAddress.setCity(newRestaurantAddress.getCity());
        restaurantAddress.setStreetName(newRestaurantAddress.getStreetName());
        restaurantAddress.setStreetAddress(newRestaurantAddress.getStreetAddress());
        restaurantAddress.setPinCode(newRestaurantAddress.getPinCode());
        restaurantAddress.setProvince(newRestaurantAddress.getProvince());
        return restaurantAddress;
    }
}
