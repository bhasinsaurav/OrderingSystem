package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantDetailsRequest;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Mappers.RestaurantMapper;
import com.ren.orderingSystem.repository.RestaurantRepository;
import com.ren.orderingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    @Autowired
    public RestaurantService(RestaurantMapper restaurantMapper, UserRepository userRepository){
        this.userRepository= userRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    public void addRestaurantDetails(AddRestaurantDetailsRequest addRestaurantDetailsRequest, UUID userId){
        Optional<User> byId = userRepository.findById(userId);
        User user = byId.get();
        Restaurant restaurantEntity = restaurantMapper.toRestaurantEntity(addRestaurantDetailsRequest);
        restaurantEntity.setUser(user);
        user.setRestaurant(restaurantEntity);
        userRepository.save(user);
    }
}
