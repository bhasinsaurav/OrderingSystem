package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterAdminRequest;
import com.ren.orderingSystem.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUserEntity(RegisterAdminRequest newRestaurant, User user){
        user.setFirstName(newRestaurant.getFirstName());
        user.setLastName(newRestaurant.getLastName());
        user.setUserName(newRestaurant.getUserName());
        user.setEmail(newRestaurant.getEmail());
        user.setPhoneNumber(newRestaurant.getPhoneNumber());
        return user;
    }
}
