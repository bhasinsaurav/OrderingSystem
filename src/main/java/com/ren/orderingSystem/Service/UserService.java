package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.RegisterAdminRequest;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RegisterAdminResponse;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Mappers.RestaurantMapper;
import com.ren.orderingSystem.Mappers.UserMapper;
import com.ren.orderingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestaurantMapper restaurantMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, RestaurantMapper restaurantMapper){
        this.userRepository = userRepository;
        this.userMapper= userMapper;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    private RegisterAdminResponse registerRestaurant(RegisterAdminRequest registerAdminRequest){
        try {
            User user = new User();
            RegisterAdminResponse registerAdminResponse = new RegisterAdminResponse();
            user.setUserTimestamp(LocalDateTime.now());
            User userEntity = userMapper.toUserEntity(registerAdminRequest, user);
            Restaurant restaurant = restaurantMapper.toRestaurantEntity(registerAdminRequest.getAddRestaurantDetailsRequest());
            restaurant.setUser(userEntity);
            userEntity.setRestaurant(restaurant);
            User savedUser = userRepository.save(userEntity);
            registerAdminResponse.setUserId(savedUser.getUserId());
            return registerAdminResponse;
        } catch (Exception e) {
            log.error("Unable to create user", e);
        }
    }
}
