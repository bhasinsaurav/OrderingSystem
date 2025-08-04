package com.ren.orderingSystem.Service;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantDetailsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateRestaurantInfoRequestDto;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantDetailsInfoResponse;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Entity.RestaurantAddress;
import com.ren.orderingSystem.Entity.User;
import com.ren.orderingSystem.Exceptions.RestaurantNotFoundException;
import com.ren.orderingSystem.Mappers.RestaurantMapper;
import com.ren.orderingSystem.repository.RestaurantRepository;
import com.ren.orderingSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class RestaurantService {

    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantMapper restaurantMapper, UserRepository userRepository, RestaurantRepository restaurantRepository){
        this.userRepository= userRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public RestaurantDetailsInfoResponse addRestaurantDetails(AddRestaurantDetailsRequest addRestaurantDetailsRequest, UUID userId){
        Optional<User> byId = userRepository.findById(userId);
        User user = byId.get();
        Restaurant restaurantEntity = restaurantMapper.toRestaurantEntity(addRestaurantDetailsRequest);
        restaurantEntity.setUser(user);
        user.setRestaurant(restaurantEntity);
        userRepository.save(user);
        Restaurant savedRestaurant = user.getRestaurant();
        return restaurantMapper.toRestaurantDetailsInfoResponse(savedRestaurant);
    }

    public void updateRestaurantDetails(UpdateRestaurantInfoRequestDto updateRestaurantInfoRequestDto, UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        User user = byId.get();
        Optional<Restaurant> byUserUserId = restaurantRepository.findByUser_UserId(userId);
        Restaurant restaurant = byUserUserId.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for user ID: " + userId));
        Restaurant updatedRestaurant = restaurantMapper.updateRestaurantFromDto(updateRestaurantInfoRequestDto, restaurant);
        Set<RestaurantAddress> restaurantAddress = updatedRestaurant.getRestaurantAddress();
        Optional<RestaurantAddress> first = restaurantAddress.stream()
                .filter(address ->
                        Objects.equals(
                                address.getRestaurantAddressId(),
                                updateRestaurantInfoRequestDto.getRestaurantAddresses().getRestaurantAddressId()
                        )
                )
                .findFirst();

        if(first.isEmpty()) {
            RestaurantAddress restaurantAddress1 = new RestaurantAddress();
        }
    }

    public RestaurantDetailsInfoResponse getRestaurantDetail(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        User user = byId.get();
        Optional<Restaurant> byUserUserId = restaurantRepository.findByUser_UserId(userId);
        Restaurant restaurant = byUserUserId.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found for user ID: " + userId));
        return restaurantMapper.toRestaurantDetailsInfoResponse(restaurant);
    }
}
