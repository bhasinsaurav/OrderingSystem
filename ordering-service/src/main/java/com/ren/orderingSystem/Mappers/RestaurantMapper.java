package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantDetailsRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateRestaurantAddressRequest;
import com.ren.orderingSystem.ApiContracts.RequestDto.UpdateRestaurantInfoRequestDto;
import com.ren.orderingSystem.ApiContracts.ResponseDto.RestaurantDetailsInfoResponse;
import com.ren.orderingSystem.Entity.Restaurant;
import com.ren.orderingSystem.Entity.RestaurantAddress;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public Restaurant toRestaurantEntity(AddRestaurantDetailsRequest newRestaurantDetails){
        Restaurant restaurant = new Restaurant();
        restaurant.setContactNumber(newRestaurantDetails.getContactNumber());
        restaurant.setRestaurantName(newRestaurantDetails.getRestaurantName());
        restaurant.setSlug(newRestaurantDetails.getSlug());
        Set<RestaurantAddress> restaurantAddresses = newRestaurantDetails.getRestaurantAddresses().stream().map(RestaurantAddressMapper::toEntity).collect(Collectors.toSet());
        restaurantAddresses.forEach(x -> x.setRestaurant(restaurant));
        restaurant.setRestaurantAddress(restaurantAddresses);
        return restaurant;
    }

    public RestaurantDetailsInfoResponse toRestaurantDetailsInfoResponse(Restaurant restaurantEntity) {
        RestaurantDetailsInfoResponse response = new RestaurantDetailsInfoResponse();
        response.setContactNumber(restaurantEntity.getContactNumber());
        response.setRestaurantName(restaurantEntity.getRestaurantName());
        response.setSlug(restaurantEntity.getSlug());
        response.setRestaurantAddressInfoResponses(restaurantEntity.getRestaurantAddress().stream()
                .map(RestaurantAddressMapper::toResponseDto)
                .collect(Collectors.toSet()));
        return response;
    }

    public Restaurant updateRestaurantFromDto(UpdateRestaurantInfoRequestDto updateRestaurantInfoRequestDto, Restaurant restaurant) {
        if(updateRestaurantInfoRequestDto.getContactNumber() != null && !updateRestaurantInfoRequestDto.getContactNumber().isEmpty()){
            restaurant.setContactNumber(updateRestaurantInfoRequestDto.getContactNumber());
        }
        if(updateRestaurantInfoRequestDto.getRestaurantName() != null && !updateRestaurantInfoRequestDto.getRestaurantName().isEmpty()){
            restaurant.setRestaurantName(updateRestaurantInfoRequestDto.getRestaurantName());
        }
        if(updateRestaurantInfoRequestDto.getSlug() != null && !updateRestaurantInfoRequestDto.getSlug().isEmpty()){
            restaurant.setSlug(updateRestaurantInfoRequestDto.getSlug());
        }
        return restaurant;
    }

//    public RestaurantAddress toRestaurantAddressEntity(UpdateRestaurantAddressRequest updateRestaurantAddressRequest) {
//        RestaurantAddress address = new RestaurantAddress();
//        if(updateRestaurantAddressRequest.get)

    }

