package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public void toUserEntity(RegisterRestaurantRequest newRestaurant, User user){
        user.setFirstName(newRestaurant.getFirstName());
        user.setLastName(newRestaurant.getLastName());
        user.setUserName(newRestaurant.getUserName());
        user.setEmail(newRestaurant.getEmail());
        user.setPhoneNumber(newRestaurant.getPhoneNumber());
    }
}
