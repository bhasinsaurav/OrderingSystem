package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterRestaurantRequest;
import com.ren.orderingSystem.Entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    public UserMapper(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public User toUserEntity(RegisterRestaurantRequest newRestaurant, User user){
        user.setFirstName(newRestaurant.getFirstName());
        user.setLastName(newRestaurant.getLastName());
        user.setUserName(newRestaurant.getEmail());
        user.setEmail(newRestaurant.getEmail());
        user.setPhoneNumber(newRestaurant.getPhoneNumber());
        return user;
    }

    public User oAuthToUser(User user, Map<String, Object> userInfo){
        user.setFirstName((String) userInfo.get("given_name"));
        user.setLastName((String) userInfo.get("family_name"));
        user.setUserTimestamp(LocalDateTime.now());
        user.setUserName((String) userInfo.get("email"));
        user.setEmail((String) userInfo.get("email"));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        return user;
    }
}
