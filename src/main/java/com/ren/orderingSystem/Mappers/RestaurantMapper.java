package com.ren.orderingSystem.Mappers;

import com.ren.orderingSystem.ApiContracts.RequestDto.AddRestaurantDetailsRequest;
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
}
